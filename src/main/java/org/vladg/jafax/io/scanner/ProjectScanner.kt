package org.vladg.jafax.io.scanner

import com.google.inject.Guice
import org.vladg.jafax.ast.ASTCreator
import org.vladg.jafax.io.writer.ProjectLayoutWriter
import org.vladg.jafax.utils.extensions.logger
import org.vladg.jafax.utils.filefinder.FileFinder
import org.vladg.jafax.utils.inject.DependencyManager
import java.nio.file.Path

object ProjectScanner {

    private var astCreator: ASTCreator

    private var projectLayoutWriter: ProjectLayoutWriter

    init {
        val injector = Guice.createInjector(DependencyManager())
        astCreator = injector.getInstance(ASTCreator::class.java)
        projectLayoutWriter = injector.getInstance(ProjectLayoutWriter::class.java)
    }

    private val logger = logger();

    fun beginScan(path: Path) {
        logger.info("Scanning for files...")
        val files = FileFinder.findFiles(path)
        val javaFiles = files.javaFiles.toTypedArray()
        val jarFiles = files.jarFiles.toTypedArray()
        astCreator.createAst(javaFiles, jarFiles)
        projectLayoutWriter.writeClasses(path)
    }

}