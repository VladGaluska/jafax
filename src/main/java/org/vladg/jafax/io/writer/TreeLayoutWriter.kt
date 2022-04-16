package org.vladg.jafax.io.writer

import kotlinx.serialization.encodeToString
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.io.converter.ProjectConverter
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.utils.extensions.getLayoutFile

class TreeLayoutWriter {

    private val format = LayoutFormat.treeFormat

    fun writeLayout(path: String, name: String) {
        val file = getLayoutFile("$name-tree")
        file.writeText(encodeObjects(path))
    }

    private fun encodeObjects(path: String): String =
        format.encodeToString(ProjectConverter.convertToTree(path))

}