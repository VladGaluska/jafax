package org.vladg.jafax.ast

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.eclipse.jdt.core.dom.ASTVisitor
import org.vladg.jafax.ast.repository.ContainerStack
import org.vladg.jafax.ast.unwrapper.AttributeUnwrapper
import org.vladg.jafax.ast.unwrapper.ClassUnwrapper
import org.vladg.jafax.ast.unwrapper.MethodInvocationUnwrapper
import org.vladg.jafax.ast.unwrapper.MethodUnwrapper
import org.vladg.jafax.io.NamePrefixTrimmer
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.repository.FileRepository
import org.vladg.jafax.repository.model.ImportStatement
import org.vladg.jafax.utils.extensions.ast.countsToComplexity
import org.vladg.jafax.utils.extensions.ast.immediateContainerBinding
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
            logger.warn(
                "Could not resolve binding for type: ${typeDeclaration.name.fullyQualifiedName}" +
                    " in file: ${currentFileName}, will ignore..."
            )
            return false
        }
        if (!binding.isClass && !binding.isInterface) return true
        val clazz = classUnwrapper.findOrCreateClassForBinding(binding, true)!!
        if (typeDeclaration.parent.nodeType == ASTNode.COMPILATION_UNIT) {
            clazz.fileName = currentFileName
            ContainerStack.clearStack()
        }
        ContainerStack.addToStack(clazz)
        return true
    }

    override fun visit(annonymousClassDeclaration: AnonymousClassDeclaration): Boolean {
        val binding = annonymousClassDeclaration.resolveBinding()
        if (binding == null) {
            logger.warn(
                "Could not resolve binding for anonymous type" +
                    " in file: $currentFileName, will ignore..."
            )
            return false
        }
        if (!binding.isClass && !binding.isInterface) return true
        val clazz = classUnwrapper.findOrCreateClassForBinding(binding, true)!!
        ContainerStack.addToStack(clazz)
        return true
    }

    override fun visit(methodDeclaration: MethodDeclaration): Boolean {
        val binding = methodDeclaration.resolveBinding()
        methodDeclaration.parameters()
        if (binding == null) {
            logger.warn(
                "Could not resolve binding for method:  ${methodDeclaration.name.fullyQualifiedName}" +
                    " in file:  $currentFileName, will ignore..."
            )
            return false
        }
        val method = methodUnwrapper.findOrCreateMethodForBinding(binding, true)!!
        ContainerStack.addToStack(method)
        return true
    }

    override fun visit(node: LambdaExpression): Boolean {
        val binding = node.resolveMethodBinding()
        if (binding == null) {
            logger.warn(
                "Could not resolve binding for lambda method in file: ${currentFileName}, " +
                    "will ignore..."
            )
            return false
        }
        val method =
            methodUnwrapper.findOrCreateMethodForBinding(binding, true, node.immediateContainerBinding().first)!!
        ContainerStack.addToStack(method)
        attributeUnwrapper.setParametersToLambda(method, node.parameters())
        return true
    }

    override fun visit(node: ParameterizedType): Boolean {
        val binding = node.resolveBinding()
        if (binding == null) {
            logger.warn(
                "Could not resolve binding for parameterized type: ${node.type}" +
                    " in file $currentFileName, will ignore..."
            )
            return false
        }
        classUnwrapper.registerParameterInstance(binding)
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

    override fun visit(node: SuperConstructorInvocation): Boolean {
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
        return visitVariableBinding(node.resolveFieldBinding(), node, node.name.fullyQualifiedName)
    }

    override fun visit(node: SuperFieldAccess): Boolean {
        return visitVariableBinding(node.resolveFieldBinding(), node, node.name.fullyQualifiedName)
    }

    override fun visit(node: QualifiedName): Boolean {
        val binding = node.resolveBinding()
        if (binding !is IVariableBinding) {
            return true
        }
        return visitVariableBinding(binding, node, node.name.fullyQualifiedName)
    }

    override fun visit(node: WhileStatement): Boolean {
        methodUnwrapper.incrementCyclomaticComplexity(node)
        return true
    }

    override fun visit(node: DoStatement): Boolean {
        methodUnwrapper.incrementCyclomaticComplexity(node)
        return true
    }

    override fun visit(node: IfStatement): Boolean {
        methodUnwrapper.incrementCyclomaticComplexity(node)
        return true
    }

    override fun visit(node: ForStatement): Boolean {
        methodUnwrapper.incrementCyclomaticComplexity(node)
        return true
    }

    override fun visit(node: EnhancedForStatement): Boolean {
        methodUnwrapper.incrementCyclomaticComplexity(node)
        return true
    }

    override fun visit(node: ConditionalExpression): Boolean {
        methodUnwrapper.incrementCyclomaticComplexity(node)
        return true
    }

    override fun visit(node: InfixExpression): Boolean {
        if (node.operator.countsToComplexity()) {
            methodUnwrapper.incrementCyclomaticComplexity(node)
        }
        return true
    }

    override fun visit(node: CatchClause): Boolean {
        methodUnwrapper.incrementCyclomaticComplexity(node)
        return true
    }

    override fun visit(node: SwitchCase): Boolean {
        methodUnwrapper.incrementCyclomaticComplexity(node)
        return true
    }

    override fun visit(node: ImportDeclaration): Boolean {
        val importStatement = ImportStatement(
            importedClass = node.name.fullyQualifiedName,
            static = node.isStatic,
            onDemand = node.isOnDemand
        )
        CommonRepository.addObject(importStatement)
        FileRepository.findOrCreate(currentFileName).addImport(
            importStatement
        )
        return true
    }

    private fun visitVariableBinding(binding: IVariableBinding?, node: ASTNode, name: String): Boolean {
        if (binding == null) {
            logger.warn(
                "Could not resolve binding for field:  $name" +
                    " in file:  $currentFileName, will ignore..."
            )
            return false
        }
        attributeUnwrapper.createFieldAccess(binding, node)
        return true
    }

}
