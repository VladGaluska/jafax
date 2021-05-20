package org.vladg.jafax.io.writer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.vladg.jafax.io.model.Metrics
import java.io.File
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.name

@OptIn(ExperimentalSerializationApi::class, ExperimentalPathApi::class)
object MetricsWriter {

    private val csv = Csv { hasHeaderRecord = true }

    fun writeMetricsToFile(metrics: List<Metrics>, path: Path, name: String) {
        getMetricsFile(path, name).writeText(csv.encodeToString(metrics))
    }

    fun readMetricsFromFile(file: File):List<Metrics> =
        csv.decodeFromString(file.readText())

    private fun getMetricsFile(path: Path, name: String) =
            File("$path/${name}-metrics.csv")

}