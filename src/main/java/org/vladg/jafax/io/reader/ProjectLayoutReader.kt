package org.vladg.jafax.io.reader

import kotlinx.serialization.decodeFromString
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.repository.model.ASTObject
import java.io.File

object ProjectLayoutReader {

    fun readLayout(file: File) =
        LayoutFormat.format.decodeFromString<List<ASTObject>>(file.readText())

}
