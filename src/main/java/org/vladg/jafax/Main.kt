package org.vladg.jafax

import org.vladg.jafax.imports.ImportsComputer
import org.vladg.jafax.io.scanner.ProjectScanner
import org.vladg.jafax.metrics.MetricsComputer
import org.vladg.jafax.relations.ExternalRelationsComputer
import org.vladg.jafax.relations.RelationsComputer
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
    ProjectScanner.beginScan(path, path.name)
    if (!onlyLayout) {
        RelationsComputer.computeRelations(Paths.get("results"), path.name)
        ExternalRelationsComputer.computeRelations(Paths.get("results"), "${path.name}-external")
        MetricsComputer.computeMetrics(Paths.get("results"), path.name)
        ImportsComputer.computeImports(Paths.get("results"), path.name)
    }


}
