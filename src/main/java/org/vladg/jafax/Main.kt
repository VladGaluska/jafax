package org.vladg.jafax

import org.vladg.jafax.io.scanner.ProjectScanner
import org.vladg.jafax.metrics.MetricsComputer
import org.vladg.jafax.relations.RelationsComputer
import java.nio.file.Path
import java.nio.file.Paths

fun getPathFromName(pathName: String): Path {
    return Paths.get(pathName).toAbsolutePath().normalize()
}

fun main(args: Array<String>) {
    val path = if (args.isNotEmpty()) getPathFromName(args[0]) else getPathFromName(".")
    ProjectScanner.beginScan(path)
    RelationsComputer.computeRelations(path)
    MetricsComputer.computeMetrics(path)
}