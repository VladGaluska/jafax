/*
package org.vladg.jafax.io.writer

import kotlinx.serialization.encodeToString
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.utils.extensions.getLayoutFile
import java.nio.file.Path

class ProjectLayoutWriter {

    private val format = LayoutFormat.format

    fun writeLayout(path: Path) {
        val file = getLayoutFile(path)
        file.writeText(encodeObjects())
    }

    private fun encodeObjects(): String =
        format.encodeToString(CommonRepository.findAll().toList())

}*/
