package org.vladg.jafax.ast

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.eclipse.jdt.core.dom.ASTVisitor
import org.vladg.jafax.ast.repository.NonPersistentRepository
import org.vladg.jafax.ast.service.AttributeService
import org.vladg.jafax.ast.service.ClassService
import org.vladg.jafax.ast.service.MethodInvocationService
import org.vladg.jafax.ast.service.MethodService
import org.vladg.jafax.utils.extensions.logger

class ASTVisitor : ASTVisitor() {

    private val logger = logger()

    @Inject
    private lateinit var classService: ClassService

    @Inject
    private lateinit var methodService: MethodService

    @Inject
    private lateinit var attributeService: AttributeService

    @Inject
    private lateinit var methodInvocationService: MethodInvocationService

    var currentFileName: String = ""

    override fun visit(typeDeclaration: TypeDeclaration): Boolean {
        val binding = typeDeclaration.resolveBinding()
        if (binding == null) {
            logger.warn("Could not resolve binding for type: ${typeDeclaration.name.fullyQualifiedName}" +
                        " in file: ${this.currentFileName}, will ignore...")
            return false
        }
        if (!binding.isClass) return true
        val clazz = this.classService.findOrCreateClassForBinding(binding, true)!!
        if (typeDeclaration.parent.nodeType == ASTNode.COMPILATION_UNIT) {
            clazz.fileName = this.currentFileName
        }
        NonPersistentRepository.addToContainer(clazz)
        return true
    }

    override fun visit(methodDeclaration: MethodDeclaration): Boolean {
        val binding = methodDeclaration.resolveBinding()
        if (binding == null) {
            logger.warn("Could not resolve binding for method:  ${methodDeclaration.name.fullyQualifiedName}" +
                        " in file:  ${this.currentFileName}, will ignore...")
            return false
        }
        val method = this.methodService.findOrCreateMethodForBinding(binding, true)!!
        NonPersistentRepository.addToContainer(method)
        return true
    }

    override fun visit(node: MethodInvocation): Boolean {
        this.methodInvocationService.registerInvocation(node)
        return true
    }

    override fun visit(node: SuperMethodInvocation): Boolean {
        this.methodInvocationService.registerInvocation(node)
        return true
    }

    override fun visit(node: ClassInstanceCreation): Boolean {
        this.methodInvocationService.registerInvocation(node)
        return true
    }

    override fun visit(node: VariableDeclarationStatement): Boolean {
        this.attributeService.findOrCreateAttribute(node, true)
        return true
    }

    override fun visit(node: SingleVariableDeclaration): Boolean {
        this.attributeService.findOrCreateAttribute(node, true)
        return true
    }

    override fun visit(node: FieldDeclaration): Boolean {
        this.attributeService.findOrCreateAttribute(node, true)
        return true
    }

    override fun visit(node: FieldAccess): Boolean {
        val binding = node.resolveFieldBinding()
        if (binding == null) {
            logger.warn("Could not resolve binding for field:  ${node.name}" +
                    " in file:  ${this.currentFileName}, will ignore...")
            return false
        }
        this.attributeService.findOrCreateFieldFromBinding(binding)
        return true
    }

}