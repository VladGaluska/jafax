package org.vladg.jafax.io.scanner

import com.google.inject.Guice
import org.vladg.jafax.ast.ASTCreator
import org.vladg.jafax.ast.repository.indexed.ContainerIndexedAttributeRepository
import org.vladg.jafax.ast.repository.indexed.KeyIndexedClassRepository
import org.vladg.jafax.ast.repository.indexed.KeyIndexedMethodRepository
import org.vladg.jafax.io.NamePrefixTrimmer
import org.vladg.jafax.io.reader.ProjectLayoutReader
import org.vladg.jafax.io.writer.ProjectLayoutWriter
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.utils.extensions.getLayoutFile
import org.vladg.jafax.utils.extensions.logger
import org.vladg.jafax.utils.filefinder.FileFinder
import org.vladg.jafax.utils.inject.DependencyManager
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

object ProjectScanner {

    private var astCreator: ASTCreator

    private var projectLayoutWriter: ProjectLayoutWriter

    init {
        val injector = Guice.createInjector(DependencyManager())
        astCreator = injector.getInstance(ASTCreator::class.java)
        projectLayoutWriter = injector.getInstance(ProjectLayoutWriter::class.java)
    }

    private val logger = logger()

    fun beginScan(path: Path, onlyLayout: Boolean) {
        val existentLayout = getLayout(path)
        if (existentLayout != null) {
            scanFromFile(existentLayout)
        } else {
            scanFromScratch(path, onlyLayout)
        }
    }

    private fun scanFromFile(file: File) {
        logger.info("Layout file found, using that instead of scanning...")
        ProjectLayoutReader.readLayout(file)
    }

    private fun scanFromScratch(path: Path, onlyLayout: Boolean) {
        logger.info("Scanning for files...")
        val files = FileFinder.findFiles(path)
        val javaFiles = files.javaFiles.toTypedArray()
        val jarFiles = files.jarFiles.toTypedArray()
        astCreator.createAst(javaFiles, jarFiles)
        trimFileNames()
        projectLayoutWriter.writeLayout(if (onlyLayout) Paths.get(".") else path)
        clearParserRepos()
    }

    private fun clearParserRepos() {
        KeyIndexedClassRepository.clear()
        KeyIndexedMethodRepository.clear()
        ContainerIndexedAttributeRepository.clear()
    }

    private fun trimFileNames() {
        ClassRepository.getAll()
                .forEach { it.fileName = it.fileName?.let { name -> NamePrefixTrimmer.trimString(name) } }
    }

    private fun getLayout(path: Path): File? {
        val file = getLayoutFile(path)
        return if (file.exists()) file
               else null
    }

}