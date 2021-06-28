package org.vladg.jafax.io.writer

import kotlinx.serialization.encodeToString
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.utils.extensions.getLayoutFile

class ProjectLayoutWriter {

    private val format = LayoutFormat.format

    fun writeLayout(name: String) {
        val file = getLayoutFile(name)
        file.writeText(encodeObjects())
    }

    private fun encodeObjects(): String =
        format.encodeToString(CommonRepository.findAll().toList())

}
