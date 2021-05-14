package org.vladg.jafax.io.reader

import kotlinx.serialization.decodeFromString
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.utils.extensions.getLayoutFile
import java.io.File
import java.nio.file.Path

object ProjectLayoutReader {

    fun readLayout(file: File) =
        LayoutFormat.format.decodeFromString<List<ASTObject>>(file.readText())

}