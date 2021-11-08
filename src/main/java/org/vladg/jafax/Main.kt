package org.vladg.jafax

import org.vladg.jafax.experimental.InterfaceComputer
import org.vladg.jafax.imports.ImportsComputer
import org.vladg.jafax.io.scanner.ProjectScanner
import org.vladg.jafax.metrics.MetricsComputer
import org.vladg.jafax.relations.ExternalRelationsComputer
import org.vladg.jafax.relations.RelationsComputer
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.name

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

    val resultsPath = Paths.get("results")
    if (!Files.exists(resultsPath))
        resultsPath.toFile().mkdirs()

    ProjectScanner.beginScan(path, path.name)
    if (!onlyLayout) {
        RelationsComputer.computeRelations(resultsPath, path.name)
        ExternalRelationsComputer.computeRelations(resultsPath, "${path.name}-external")
        MetricsComputer.computeMetrics(resultsPath, path.name)
        ImportsComputer.computeImports(resultsPath, path.name)
        InterfaceComputer.computeImports(resultsPath, path.name)
    }

    println("Results can be found at ${resultsPath.toAbsolutePath()}")

}
