package org.vladg.jafax.ast.unwrapper

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.vladg.jafax.ast.repository.ContainerStack
import org.vladg.jafax.ast.repository.indexed.ContainerIndexedAttributeRepository
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Attribute.AttributeKind
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Container
import org.vladg.jafax.repository.model.Method
import org.vladg.jafax.utils.extensions.ast.findParentOfType
import org.vladg.jafax.utils.extensions.ast.getAttributeKind
import org.vladg.jafax.utils.extensions.ast.getName
import org.vladg.jafax.utils.extensions.ast.modifierSet
import org.vladg.jafax.utils.extensions.logger

class AttributeUnwrapper {

    private val logger = logger()

    @Inject
    private lateinit var classUnwrapper: ClassUnwrapper

    @Inject
    private lateinit var containerService: ContainerService

    private fun findAttributeByParentAndName(parentKey: String?, name: String?): Attribute? {
        return ContainerIndexedAttributeRepository.findByParentKeyAndName(
                parentKey ?: return null,
                name ?: return null
        )
    }

    private fun addAttributeWithRecursiveSafety(attribute: Attribute, typeSupplier: () -> Class?, containerSupplier: () -> Container?): Attribute {
        attribute.container = containerSupplier()
        ContainerIndexedAttributeRepository.addAttribute(attribute)
        attribute.type = typeSupplier()
        setAttributeToItsContainer(attribute)
        return attribute
    }

    private fun setAttributeToItsContainer(attribute: Attribute) =
            attribute.container?.addToContainedAttributes(attribute)

    private fun createAttribute(fieldBinding: IVariableBinding, kind: AttributeKind, containerSupplier: () -> Container?): Attribute =
            addAttributeWithRecursiveSafety(
                    Attribute(
                            name = fieldBinding.name,
                            modifiers = fieldBinding.modifierSet(),
                            kind = kind
                    ),
                    { classUnwrapper.findOrCreateClassForBinding(fieldBinding.type) },
                    containerSupplier
            )


    private fun createAccessedField(fieldBinding: IVariableBinding): Attribute =
            createAttribute(fieldBinding, AttributeKind.Field) {
                containerService.getOrCreateContainerForBinding(fieldBinding.declaringClass)
            }

    private fun createAttribute(node: VariableDeclarationStatement, containerSupplier: () -> Container?) =
            addAttributeWithRecursiveSafety(
                    Attribute(
                            name = node.getName()!!,
                            type = findClass(node.type),
                            modifiers = node.modifierSet(),
                            container = containerSupplier(),
                            kind = AttributeKind.LocalVariable
                    ),
                    { findClass(node.type) },
                    containerSupplier
            )

    private fun createAttribute(node: FieldDeclaration, containerSupplier: () -> Container?) =
            addAttributeWithRecursiveSafety(
                    Attribute(
                            name = node.getName()!!,
                            type = findClass(node.type),
                            modifiers = node.modifierSet(),
                            container = containerSupplier(),
                            kind = AttributeKind.Field
                    ),
                    { findClass(node.type) },
                    containerSupplier
            )

    private fun createAttribute(node: SingleVariableDeclaration, kind: AttributeKind? = null, containerSupplier: () -> Container?) =
            addAttributeWithRecursiveSafety(
                    Attribute(
                            name = node.name.fullyQualifiedName,
                            type = findClass(node.type),
                            modifiers = node.modifierSet(),
                            container = containerSupplier(),
                            kind = kind ?: node.getAttributeKind()
                    ),
                    { findClass(node.type) },
                    containerSupplier
            )

    private fun findClass(type: Type) = classUnwrapper.findOrCreateClassForBinding(type.resolveBinding())

    fun findOrCreateAttribute(node: VariableDeclarationStatement, useStack: Boolean = false): Attribute? {
        val parentBinding = getBindingForContainer(node.findParentOfType<MethodDeclaration>() ?: return null)
        return findAttributeByParentAndName(parentBinding?.key, node.getName()) ?: createAttribute(node) {
            if (!useStack) containerService.getOrCreateContainerForBinding(parentBinding)
            else ContainerStack.popUntilBindingObject(parentBinding)
        }
    }

    fun findOrCreateAttribute(node: FieldDeclaration, useStack: Boolean = false): Attribute? {
        val parentBinding = getBindingForContainer(node.findParentOfType<TypeDeclaration>() ?: return null)
        return findAttributeByParentAndName(parentBinding?.key, node.getName()) ?: createAttribute(node) {
            if (!useStack) containerService.getOrCreateContainerForBinding(parentBinding)
            else ContainerStack.popUntilBindingObject(parentBinding)
        }
    }

    fun findOrCreateAttribute(node: SingleVariableDeclaration, useStack: Boolean = false): Attribute? {
        val parentBinding = getBindingForContainer(node.findParentOfType<MethodDeclaration>() ?: return null)
        return findAttributeByParentAndName(parentBinding?.key, node.name.fullyQualifiedName) ?: createAttribute(node) {
            if (!useStack) containerService.getOrCreateContainerForBinding(parentBinding)
            else ContainerStack.popUntilBindingObject(parentBinding)
        }
    }

    private fun findOrCreateFieldFromBinding(fieldBinding: IVariableBinding): Attribute {
        return findAttributeByParentAndName(fieldBinding.declaringClass?.key, fieldBinding.name)
                ?: createAccessedField(fieldBinding)
    }

    fun createFieldAccess(binding: IVariableBinding, node: ASTNode) =
            addAccessedField(node, findOrCreateFieldFromBinding(binding))

    private fun addAccessedField(node: ASTNode, field: Attribute) =
            containerService.findContainer(node)?.addToAccessedFields(field)

    private fun getBindingForContainer(node: ASTNode?): IBinding? {
        node ?: return null
        val binding = when (node) {
            is MethodDeclaration -> node.resolveBinding()
            is TypeDeclaration -> node.resolveBinding()
            else -> throw IllegalStateException("Type ${node.nodeType} not supported for attribute container")
        }
        if (binding == null) {
            logger.warn("Could not resolve binding for ${binding?.name}")
        }
        return binding
    }

    fun setParametersToLambda(lambda: Method, parameters: List<*>) {
        if (parameters.isNotEmpty()) {
            lambda.parameters.addAll(
                    if (parameters[0] is SingleVariableDeclaration) {
                        parameters.map { it as SingleVariableDeclaration }
                                .map { createLambdaParameter(lambda, it) }
                    } else {
                        parameters.map { it as VariableDeclarationFragment }
                                .map { createLambdaParameter(lambda, it) }
                    }
            )
        }
    }

    private fun createLambdaParameter(lambda: Method, variableDeclaration: SingleVariableDeclaration) =
            findAttributeByParentAndName(lambda.key, variableDeclaration.name.fullyQualifiedName)
                    ?: createAttribute(variableDeclaration, AttributeKind.Parameter) { lambda }

    private fun createLambdaParameter(lambda: Method, fragment: VariableDeclarationFragment) =
            findAttributeByParentAndName(lambda.key, fragment.name.fullyQualifiedName)
                    ?: createAttribute(fragment.resolveBinding(), AttributeKind.Parameter) { lambda }

}