package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.*
import org.vladg.jafax.repository.model.Modifier

fun MethodDeclaration.signature(): String {
    return name.fullyQualifiedName + "(" + parametersString() + ")"
}

fun MethodDeclaration.parametersString(): String {
    return parameters().joinToString(", ") { it.toString() }
}

fun IMethodBinding.signature(): String {
    return name + "(" + parametersString() + ")"
}

fun IMethodBinding.parametersString(): String {
    return parameterTypes.joinToString(", ") { it.qualifiedName }
}

fun IMethodBinding.modifierSet(): Set<Modifier> {
    return modifierSetForValue(modifiers)
}

fun IMethodBinding.getParent(): IBinding? {
    return when {
        declaringClass != null -> declaringClass
        else -> declaringMember
    }
}
