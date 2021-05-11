package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.IBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.vladg.jafax.repository.model.Modifier

fun ITypeBinding.modifierSet(): Set<Modifier> {
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
    return if(isArray) elementType
           else this
}

fun ITypeBinding.originalType(): ITypeBinding? {
    return if (!isTypeVariable) erasure
           else this
}