package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.Modifier
import java.lang.IllegalStateException

fun Modifier.toStringValue(): String {
    return when {
        isPrivate -> "private"
        isPublic -> "public"
        isProtected -> "protected"
        isDefault -> "default"
        isStatic -> "static"
        isFinal -> "final"
        isAbstract -> "abstract"
        isTransient -> "transient"
        isNative -> "native"
        isSynchronized -> "synchronized"
        isVolatile -> "volatile"
        isStrictfp -> "strictfp"
        else -> throw IllegalStateException("There is no match for modifier: " + this.keyword)
    }
}

fun modifierSetForValue(modifiers: Int): Set<String> {
    val modifierSet = HashSet<String>()
    when {
        modifiers and Modifier.STATIC == 1 -> modifierSet.add("static")
        modifiers and Modifier.PUBLIC == 1 -> modifierSet.add("public")
        modifiers and Modifier.PRIVATE == 1 -> modifierSet.add("private")
        modifiers and Modifier.PROTECTED == 1 -> modifierSet.add("protected")
        modifiers and Modifier.DEFAULT == 1 -> modifierSet.add("default")
        modifiers and Modifier.FINAL == 1 -> modifierSet.add("final")
        modifiers and Modifier.ABSTRACT == 1 -> modifierSet.add("abstract")
        modifiers and Modifier.TRANSIENT == 1 -> modifierSet.add("transient")
        modifiers and Modifier.NATIVE == 1 -> modifierSet.add("native")
        modifiers and Modifier.SYNCHRONIZED == 1 -> modifierSet.add("synchronized")
        modifiers and Modifier.VOLATILE == 1 -> modifierSet.add("volatile")
        modifiers and Modifier.STRICTFP == 1 -> modifierSet.add("strictfp")
    }
    return modifierSet
}