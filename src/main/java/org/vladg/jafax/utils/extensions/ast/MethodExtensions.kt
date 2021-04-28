package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.*
import kotlin.streams.toList

fun MethodDeclaration.modifierSet(): List<String> {
    return (modifiers() as List<IExtendedModifier>).stream()
        .filter { it.isModifier }
        .map { (it as Modifier).toStringValue() }
        .toList()
}

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

fun IMethodBinding.modifierSet(): Set<String> {
    return modifierSetForValue(modifiers)
}

fun IMethodBinding.getParent(): IBinding? {
    return when{
        declaringClass != null -> declaringClass
        else -> declaringMember
    }
}