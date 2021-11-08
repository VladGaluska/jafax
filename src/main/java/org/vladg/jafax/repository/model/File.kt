package org.vladg.jafax.repository.model

import kotlinx.serialization.Serializable
import org.vladg.jafax.io.serializers.FileSerializer

@Serializable(with = FileSerializer::class)
class File(
    path: String = "",
    var imports: MutableList<ImportStatement> = mutableListOf()
) : ASTObject(path, emptySet(), null) {
    fun addImport(importStatement: ImportStatement) {
        imports.add(importStatement)
    }
}
