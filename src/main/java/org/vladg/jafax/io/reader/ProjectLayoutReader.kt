package org.vladg.jafax.io.reader

import kotlinx.serialization.decodeFromString
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.utils.extensions.getLayoutFile
import java.nio.file.Path

class ProjectLayoutReader {

    private val format = LayoutFormat.format

    fun readLayout(path: Path) {
        val file = getLayoutFile(path)
        format.decodeFromString<List<ASTObject>>(file.readText())
    }
}