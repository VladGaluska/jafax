package org.vladg.jafax.utils.extensions.ast

import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.ImportDeclaration
import kotlin.streams.toList

fun CompilationUnit.importList(): List<String> {
    return (imports() as List<ImportDeclaration>)
        .stream()
        .map { it.name.fullyQualifiedName }
        .toList()
}

fun CompilationUnit.packageName(): String? {
    val astPackage = getPackage()
    return astPackage?.name?.fullyQualifiedName
}