package org.vladg.jafax

import org.vladg.jafax.io.scanner.ProjectScanner
import java.nio.file.Path
import java.nio.file.Paths

fun getPathFromName(pathName: String): Path {
    return Paths.get(pathName).toAbsolutePath().normalize()
}

fun main(args: Array<String>) {
    val path = if (args.isNotEmpty()) getPathFromName(args[0]) else getPathFromName(".")
    ProjectScanner.beginScan(path)
    RelationsCalculator.calculateRelations()
}