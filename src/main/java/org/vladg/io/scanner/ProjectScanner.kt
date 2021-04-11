package org.vladg.io.scanner

import org.vladg.ast.ASTCreator
import org.vladg.jafax.utils.filefinder.FileFinder
import org.vladg.logger
import java.nio.file.Path

object ProjectScanner {

    private val logger = logger();

    fun beginScan(path: Path) {
        logger.info("Scanning for files...")
        val files = FileFinder.findFiles(path)
        val javaFiles = files.javaFiles.toTypedArray()
        val jarFiles = files.jarFiles.toTypedArray()
        ASTCreator.createAst(javaFiles, jarFiles)
    }

}