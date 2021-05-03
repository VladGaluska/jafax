package org.vladg.jafax.ast

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.eclipse.jdt.core.dom.ASTVisitor
import org.vladg.jafax.ast.repository.ContainerStack
import org.vladg.jafax.ast.unwrapper.AttributeUnwrapper
import org.vladg.jafax.ast.unwrapper.ClassUnwrapper
import org.vladg.jafax.ast.unwrapper.MethodInvocationUnwrapper
import org.vladg.jafax.ast.unwrapper.MethodUnwrapper
import org.vladg.jafax.utils.extensions.logger

class ASTVisitor : ASTVisitor() {

    private val logger = logger()

    @Inject
    private lateinit var classUnwrapper: ClassUnwrapper

    @Inject
    private lateinit var methodUnwrapper: MethodUnwrapper

    @Inject
    private lateinit var attributeUnwrapper: AttributeUnwrapper

    @Inject
    private lateinit var methodInvocationUnwrapper: MethodInvocationUnwrapper

    var currentFileName: String = ""

    override fun visit(typeDeclaration: TypeDeclaration): Boolean {
        val binding = typeDeclaration.resolveBinding()
        if (binding == null) {
            logger.warn("Could not resolve binding for type: ${typeDeclaration.name.fullyQualifiedName}" +
                        " in file: ${currentFileName}, will ignore...")
            return false
        }
        if (!binding.isClass) return true
        val clazz = classUnwrapper.findOrCreateClassForBinding(binding, true)!!
        if (typeDeclaration.parent.nodeType == ASTNode.COMPILATION_UNIT) {
            clazz.fileName = currentFileName
        }
        ContainerStack.addToContainer(clazz)
        return true
    }

    override fun visit(methodDeclaration: MethodDeclaration): Boolean {
        val binding = methodDeclaration.resolveBinding()
        if (binding == null) {
            logger.warn("Could not resolve binding for method:  ${methodDeclaration.name.fullyQualifiedName}" +
                        " in file:  ${currentFileName}, will ignore...")
            return false
        }
        val method = methodUnwrapper.findOrCreateMethodForBinding(binding, true)!!
        ContainerStack.addToContainer(method)
        return true
    }

    override fun visit(node: MethodInvocation): Boolean {
        methodInvocationUnwrapper.registerInvocation(node)
        return true
    }

    override fun visit(node: SuperMethodInvocation): Boolean {
        methodInvocationUnwrapper.registerInvocation(node)
        return true
    }

    override fun visit(node: ClassInstanceCreation): Boolean {
        methodInvocationUnwrapper.registerInvocation(node)
        return true
    }

    override fun visit(node: VariableDeclarationStatement): Boolean {
        attributeUnwrapper.findOrCreateAttribute(node, true)
        return true
    }

    override fun visit(node: SingleVariableDeclaration): Boolean {
        attributeUnwrapper.findOrCreateAttribute(node, true)
        return true
    }

    override fun visit(node: FieldDeclaration): Boolean {
        attributeUnwrapper.findOrCreateAttribute(node, true)
        return true
    }

    override fun visit(node: FieldAccess): Boolean {
        val binding = node.resolveFieldBinding()
        if (binding == null) {
            logger.warn("Could not resolve binding for field:  ${node.name}" +
                    " in file:  ${currentFileName}, will ignore...")
            return false
        }
        attributeUnwrapper.createFieldAccess(binding, node)
        return true
    }

}