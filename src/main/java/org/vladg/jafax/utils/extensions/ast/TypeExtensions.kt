package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.IBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.vladg.jafax.repository.model.Modifier

fun ITypeBinding.modifierSet(): Set<Modifier> {
    return modifierSetForValue(modifiers)
}

fun ITypeBinding.getParent(): IBinding? {
    return when{
        declaringMethod != null -> declaringMethod
        declaringClass != null -> declaringClass
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