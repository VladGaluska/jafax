package org.vladg.jafax.io.reader

import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.utils.extensions.getLayoutFile
import org.vladg.jafax.utils.extensions.logger
import java.nio.file.Path

class ProjectLayoutReader {

    private val format = LayoutFormat.format

    private val logger = logger()

    fun readLayout(path: Path) {
        val file = getLayoutFile(path)
        val json = format.parseToJsonElement(file.readText())
        json.jsonArray.forEach { logger.info(it.jsonObject["type"].toString()) }
    }
}