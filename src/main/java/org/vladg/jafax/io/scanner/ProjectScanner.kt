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
import org.vladg.jafax.repository.FileRepository
import org.vladg.jafax.utils.extensions.getLayoutFile
import org.vladg.jafax.utils.extensions.logger
import org.vladg.jafax.utils.filefinder.FileFinder
import org.vladg.jafax.utils.inject.DependencyManager
import java.io.File
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.isRegularFile

object ProjectScanner {

    private var astCreator: ASTCreator

    private var projectLayoutWriter: ProjectLayoutWriter

    init {
        val injector = Guice.createInjector(DependencyManager())
        astCreator = injector.getInstance(ASTCreator::class.java)
        projectLayoutWriter = injector.getInstance(ProjectLayoutWriter::class.java)
    }

    private val logger = logger()

    private lateinit var namePrefixTrimmer: NamePrefixTrimmer

    @OptIn(ExperimentalPathApi::class)
    fun beginScan(path: Path, name: String) {
        val existentLayout =
            if (path.toFile().exists() && path.isRegularFile())
                path.toFile()
            else
                getLayout(name)
        if (existentLayout != null) {
            scanFromFile(existentLayout)
        } else {
            namePrefixTrimmer = NamePrefixTrimmer("$path/")
            FileRepository.namePrefixTrimmer = namePrefixTrimmer
            scanFromScratch(path, name)
        }
    }

    private fun scanFromFile(file: File) {
        logger.info("Layout file found, using that instead of scanning...")
        ProjectLayoutReader.readLayout(file)
    }

    private fun scanFromScratch(path: Path, name: String) {
        logger.info("Scanning for files...")
        val files = FileFinder.findFiles(path)
        val javaFiles = files.javaFiles.toTypedArray()
        val jarFiles = files.jarFiles.toTypedArray()
        astCreator.createAst(javaFiles, jarFiles)
        trimFileNames()
        projectLayoutWriter.writeLayout(name)
        clearParserRepos()
    }

    private fun clearParserRepos() {
        KeyIndexedClassRepository.clear()
        KeyIndexedMethodRepository.clear()
        ContainerIndexedAttributeRepository.clear()
    }


    private fun trimFileNames() {
        ClassRepository.getAll()
            .forEach { it.fileName = it.fileName?.let { name -> namePrefixTrimmer.trimString(name) } }
    }

    private fun getLayout(name: String): File? {
        val file = getLayoutFile(name)
        return if (file.exists()) file
        else null
    }

}
