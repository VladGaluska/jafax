package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.Modifier
import org.vladg.jafax.repository.model.Modifier as ASTModifier

fun modifierSetForValue(modifiers: Int): Set<ASTModifier> {
    val modifierSet = HashSet<ASTModifier>()
    if (modifiers and Modifier.STATIC != 0) modifierSet.add(ASTModifier.Static)
    if (modifiers and Modifier.PUBLIC != 0) modifierSet.add(ASTModifier.Public)
    if (modifiers and Modifier.PRIVATE != 0) modifierSet.add(ASTModifier.Private)
    if (modifiers and Modifier.PROTECTED != 0) modifierSet.add(ASTModifier.Protected)
    if (modifiers and Modifier.DEFAULT != 0) modifierSet.add(ASTModifier.Default)
    if (modifiers and Modifier.FINAL != 0) modifierSet.add(ASTModifier.Final)
    if (modifiers and Modifier.ABSTRACT != 0) modifierSet.add(ASTModifier.Abstract)
    if (modifiers and Modifier.TRANSIENT != 0) modifierSet.add(ASTModifier.Transient)
    if (modifiers and Modifier.NATIVE != 0) modifierSet.add(ASTModifier.Native)
    if (modifiers and Modifier.SYNCHRONIZED != 0) modifierSet.add(ASTModifier.Synchronized)
    if (modifiers and Modifier.VOLATILE != 0) modifierSet.add(ASTModifier.Volatile)
    if (modifiers and Modifier.STRICTFP != 0) modifierSet.add(ASTModifier.Strictfp)
    return modifierSet
}