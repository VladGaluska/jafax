package org.vladg.jafax

import org.vladg.jafax.experimental.InterfaceComputer
import org.vladg.jafax.imports.ImportsComputer
import org.vladg.jafax.io.scanner.ProjectScanner
import org.vladg.jafax.metrics.MetricsComputer
import org.vladg.jafax.relations.ExternalRelationsComputer
import org.vladg.jafax.relations.RelationsComputer
import org.vladg.jafax.utils.extensions.stream
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.name

@ExperimentalPathApi
fun main(args: Array<String>) {
    var path = Paths.get(".")
    var onlyLayout = false
    var treeLayout = false
    if (args.isNotEmpty()) {
        args.stream()
            .forEach {
                when (it) {
                    "-OL" -> onlyLayout = true
                    "-TL" -> treeLayout = true
                    else -> path = Paths.get(it)
                }
                if (it === "-OL") onlyLayout = true
            }
    }

    ProjectScanner.beginScan(path, treeLayout, path.name)
    if (!onlyLayout) {
        RelationsComputer.computeRelations(Paths.get("results"), path.name)
        ExternalRelationsComputer.computeRelations(Paths.get("results"), "${path.name}-external")
        MetricsComputer.computeMetrics(Paths.get("results"), path.name)
        ImportsComputer.computeImports(Paths.get("results"), path.name)
        InterfaceComputer.computeImports(Paths.get("results"), path.name)
    }


}
