package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.*
import kotlin.streams.toList

fun TypeDeclaration.modifierSet(): List<String> {
    return (modifiers() as List<IExtendedModifier>).stream()
                      .filter { it.isModifier }
                      .map { (it as Modifier).toStringValue() }
                      .toList()
}

fun ITypeBinding.modifierSet(): Set<String> {
    return modifierSetForValue(modifiers)
}

fun ITypeBinding.getParent(): IBinding? {
    return when{
        declaringClass != null -> declaringClass
        declaringMethod != null -> declaringMethod
        else -> declaringMember
    }
}

fun ITypeBinding.getActualType(): ITypeBinding {
    return if(isArray) this.elementType
           else this
}