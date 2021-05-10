package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.*

fun bindingNameFromType(kind: Int): String {
    return when(kind) {
        IBinding.ANNOTATION -> "annotation"
        IBinding.MEMBER_VALUE_PAIR -> "member value pair"
        IBinding.MODULE -> "module"
        IBinding.PACKAGE -> "package"
        IBinding.VARIABLE -> "variable"
        IBinding.TYPE -> "type"
        IBinding.METHOD -> "method"
        else -> ""
    }
}

inline fun <reified T> ASTNode.findParentOfType(): T? {
    var iteratedParent =  parent
    while (iteratedParent != null) {
        when(iteratedParent) {
            is T -> return iteratedParent
            else -> iteratedParent = iteratedParent.parent
        }
    }
    return null
}

fun ASTNode.immediateContainerBinding(): Pair<IBinding?, Boolean> {
    var iteratedParent =  parent
    while (iteratedParent != null) {
        when(iteratedParent) {
            is MethodDeclaration -> return Pair(iteratedParent.resolveBinding(), true)
            is TypeDeclaration -> return Pair(iteratedParent.resolveBinding(), true)
            else -> iteratedParent = iteratedParent.parent
        }
    }
    return Pair(null, false)
}

fun InfixExpression.Operator.countsToComplexity() =
        this == InfixExpression.Operator.AND || this == InfixExpression.Operator.OR