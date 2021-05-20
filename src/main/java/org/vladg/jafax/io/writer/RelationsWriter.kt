package org.vladg.jafax.io.writer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.vladg.jafax.io.model.Relations
import java.io.File
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.name

@OptIn(ExperimentalSerializationApi::class, ExperimentalPathApi::class)
object RelationsWriter {

    private val csv = Csv {
        hasHeaderRecord = true
        ignoreUnknownColumns = true
    }

    fun writeRelationsToFile(relations: List<Relations>, path: Path) {
        getRelationsFile(path).writeText(csv.encodeToString(relations))
    }

    fun readRelationsFromFile(file: File): List<Relations> =
            csv.decodeFromString(file.readText())

    private fun getRelationsFile(path: Path) =
            File("$path/${path.name}-relations.csv")

}