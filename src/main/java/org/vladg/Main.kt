package org.vladg

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.vladg.io.scanner.ProjectScanner
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Stream

fun getPathFromName(pathName: String): Path {
    return Paths.get(pathName).toAbsolutePath().normalize()
}

fun main(args: Array<String>) {
    val path = if (args.isNotEmpty()) getPathFromName(args[0]) else getPathFromName(".")
    ProjectScanner.beginScan(path)
}

inline fun <reified T> T.logger(): Logger {
    if (T::class.isCompanion) {
        return LoggerFactory.getLogger(T::class.java.enclosingClass)
    }
    return LoggerFactory.getLogger(T::class.java)
}

fun <T> Array<T>.stream(): Stream<T> = Arrays.stream(this)
