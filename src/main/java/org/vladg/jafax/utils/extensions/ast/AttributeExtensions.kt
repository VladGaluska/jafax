package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.*
import org.vladg.jafax.repository.model.Attribute.AttributeKind
import org.vladg.jafax.repository.model.Modifier

fun FieldDeclaration.getName(): String? = getNameFromFragment(fragments()[0])

fun VariableDeclarationStatement.getName(): String? = getNameFromFragment(fragments()[0])

private fun getNameFromFragment(fragment: Any?): String? {
    return (fragment as VariableDeclarationFragment).name.fullyQualifiedName
}

fun VariableDeclarationStatement.modifierSet(): Set<Modifier> {
    return modifierSetForValue(modifiers)
}

fun FieldDeclaration.modifierSet(): Set<Modifier> {
    return modifierSetForValue(modifiers)
}

fun SingleVariableDeclaration.modifierSet(): Set<Modifier> {
    return modifierSetForValue(modifiers)
}

fun SingleVariableDeclaration.isParameter(): Boolean {
    return when(parent.nodeType) {
        ASTNode.METHOD_DECLARATION -> true
        else -> false
    }
}

fun SingleVariableDeclaration.getAttributeKind(): AttributeKind {
    return if(isParameter()) AttributeKind.Parameter
           else AttributeKind.LocalVariable
}

fun IVariableBinding.modifierSet(): Set<Modifier> {
    return modifierSetForValue(modifiers)
}