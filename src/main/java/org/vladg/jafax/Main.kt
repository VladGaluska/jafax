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
    val onlyLayout = if (args.size > 1) args[1] == "-OL"
                     else false
    ProjectScanner.beginScan(path, onlyLayout)
    if (!onlyLayout) {
        RelationsComputer.computeRelations(Paths.get("."))
        MetricsComputer.computeMetrics(Paths.get("."))
    }
}