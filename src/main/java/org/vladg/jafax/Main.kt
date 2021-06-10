package org.vladg.jafax

import org.vladg.jafax.io.scanner.ProjectScanner
import org.vladg.jafax.metrics.MetricsComputer
import org.vladg.jafax.relations.RelationsComputer
import org.vladg.jafax.repository.ClassRepository
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.name

fun getPathFromName(pathName: String): Path {
    return Paths.get(pathName).toAbsolutePath().normalize()
}

@ExperimentalPathApi
fun main(args: Array<String>) {
    var path = Paths.get(".")
    var onlyLayout = false
    if (args.isNotEmpty()) {
        if (args.size > 1) {
            path = Paths.get(args[0])
            if (args[1] == "-OL") onlyLayout = true
        } else {
            if (args[0] == "-OL") onlyLayout = true
            else path = Paths.get(args[0])
        }
    }
    ProjectScanner.beginScan(path, path.name)
    if (!onlyLayout) {
        RelationsComputer.computeRelations(Paths.get("results"), path.name)
        MetricsComputer.computeMetrics(Paths.get("results"), path.name)
    }
}
