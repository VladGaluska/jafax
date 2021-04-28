package org.vladg.jafax.ast.service

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.vladg.jafax.ast.repository.AttributeRepository
import org.vladg.jafax.ast.repository.NonPersistentRepository
import org.vladg.jafax.ast.repository.model.Attribute
import org.vladg.jafax.ast.repository.model.Attribute.AttributeKind
import org.vladg.jafax.ast.repository.model.Container
import org.vladg.jafax.utils.extensions.ast.findParentOfType
import org.vladg.jafax.utils.extensions.ast.getAttributeKind
import org.vladg.jafax.utils.extensions.ast.getName
import org.vladg.jafax.utils.extensions.ast.modifierSet
import org.vladg.jafax.utils.extensions.logger

class AttributeService {

    private val logger = logger()

    @Inject
    private lateinit var attributeRepository: AttributeRepository

    @Inject
    private lateinit var classService: ClassService

    @Inject
    private lateinit var containerService: ContainerService

    private fun findAttributeByParentAndName(parentKey: String?, name: String?): Attribute? {
        return this.attributeRepository.findByParentKeyAndName(
            parentKey ?: return null,
            name ?: return null
        )
    }

    private fun addAttribute(attribute: Attribute): Attribute {
        this.attributeRepository.addAttribute(attribute)
        attribute.container?.addToContainedAttributes(attribute)
        return attribute
    }

    private fun createAccessedField(fieldBinding: IVariableBinding): Attribute {
        return addAttribute(Attribute(
            name = fieldBinding.name,
            type = this.classService.findOrCreateClassForBinding(fieldBinding.type),
            modifiers = fieldBinding.modifierSet(),
            container = this.containerService.getOrCreateContainerForBinding(fieldBinding.declaringClass),
            kind = AttributeKind.Field
        ))
    }

    private fun createAttribute(node: VariableDeclarationStatement, containerSupplier: () -> Container?): Attribute {
        return addAttribute(Attribute(
            name = node.getName()!!,
            type = this.findClass(node.type),
            modifiers = node.modifierSet(),
            container = containerSupplier(),
            kind = AttributeKind.LocalVariable
        ))
    }

    private fun createAttribute(node: FieldDeclaration, containerSupplier: () -> Container?): Attribute {
        return addAttribute(Attribute(
            name = node.getName()!!,
            type = this.findClass(node.type),
            modifiers = node.modifierSet(),
            container = containerSupplier(),
            kind = AttributeKind.Field
        ))
    }

    private fun createAttribute(node: SingleVariableDeclaration, containerSupplier: () -> Container?): Attribute {
        return addAttribute(Attribute(
            name = node.name.fullyQualifiedName,
            type = this.findClass(node.type),
            modifiers = node.modifierSet(),
            container = containerSupplier(),
            kind = node.getAttributeKind()
        ))
    }

    private fun findClass(type: Type) = this.classService.findOrCreateClassForBinding(type.resolveBinding())

    fun findOrCreateAttribute(node: VariableDeclarationStatement, useStack: Boolean = false): Attribute? {
        val parentBinding = getBindingForContainer(node.findParentOfType<MethodDeclaration>() ?: return null)
        return this.findAttributeByParentAndName(parentBinding?.key, node.getName()) ?:
               this.createAttribute(node) {
                   if (useStack) NonPersistentRepository.popUntilBindingObject(parentBinding)
                   else this.containerService.getOrCreateContainerForBinding(parentBinding)
               }
    }

    fun findOrCreateAttribute(node: FieldDeclaration, useStack: Boolean = false): Attribute? {
        val parentBinding = getBindingForContainer(node.findParentOfType<TypeDeclaration>() ?: return null)
        return this.findAttributeByParentAndName(parentBinding?.key, node.getName()) ?:
               this.createAttribute(node) {
                    if (useStack) NonPersistentRepository.popUntilBindingObject(parentBinding)
                    else this.containerService.getOrCreateContainerForBinding(parentBinding)
               }
    }

    fun findOrCreateAttribute(node: SingleVariableDeclaration, useStack: Boolean = false): Attribute? {
        val parentBinding = getBindingForContainer(node.findParentOfType<MethodDeclaration>() ?: return null)
        return this.findAttributeByParentAndName(parentBinding?.key, node.name.fullyQualifiedName) ?:
               this.createAttribute(node) {
                   if (useStack) NonPersistentRepository.popUntilBindingObject(parentBinding)
                   else this.containerService.getOrCreateContainerForBinding(parentBinding)
               }
    }

    fun findOrCreateFieldFromBinding(fieldBinding: IVariableBinding): Attribute {
        return this.findAttributeByParentAndName(fieldBinding.declaringClass?.key, fieldBinding.name) ?:
               this.createAccessedField(fieldBinding)
    }

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

}