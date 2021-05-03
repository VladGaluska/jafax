package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.Modifier
import org.vladg.jafax.repository.model.Modifier as ASTModifier

fun modifierSetForValue(modifiers: Int): Set<ASTModifier> {
    val modifierSet = HashSet<ASTModifier>()
    when {
        modifiers and Modifier.STATIC == 1 -> modifierSet.add(ASTModifier.Static)
        modifiers and Modifier.PUBLIC == 1 -> modifierSet.add(ASTModifier.Public)
        modifiers and Modifier.PRIVATE == 1 -> modifierSet.add(ASTModifier.Private)
        modifiers and Modifier.PROTECTED == 1 -> modifierSet.add(ASTModifier.Protected)
        modifiers and Modifier.DEFAULT == 1 -> modifierSet.add(ASTModifier.Default)
        modifiers and Modifier.FINAL == 1 -> modifierSet.add(ASTModifier.Final)
        modifiers and Modifier.ABSTRACT == 1 -> modifierSet.add(ASTModifier.Abstract)
        modifiers and Modifier.TRANSIENT == 1 -> modifierSet.add(ASTModifier.Transient)
        modifiers and Modifier.NATIVE == 1 -> modifierSet.add(ASTModifier.Native)
        modifiers and Modifier.SYNCHRONIZED == 1 -> modifierSet.add(ASTModifier.Synchronized)
        modifiers and Modifier.VOLATILE == 1 -> modifierSet.add(ASTModifier.Volatile)
        modifiers and Modifier.STRICTFP == 1 -> modifierSet.add(ASTModifier.Strictfp)
    }
    return modifierSet
}