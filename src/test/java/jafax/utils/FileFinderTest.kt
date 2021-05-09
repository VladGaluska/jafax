package jafax.utils

import jafax.getComplexProjectPath
import org.junit.Assert.assertEquals
import org.junit.Test
import org.vladg.jafax.utils.filefinder.FileFinder.findFiles
import java.nio.file.Paths
import java.util.stream.Collectors

class FileFinderTest {

    @Test
    fun shouldFindJavaFiles() {
        val files = findFiles(Paths.get("src/test/resources/org/radum"))
        val javaFiles = files.javaFiles
        assertEquals(6, javaFiles.size)
        assertEquals(0, files.jarFiles.size)
        val classNames = getSimpleFileNames(javaFiles)
        assertEquals(EXPECTED_FILE_NAMES, classNames)
    }

    @Test
    fun shouldFindSingleJavaFile() {
        val files = findFiles(Paths.get("src/test/resources/org/radum/empty/empty1"))
        val javaFiles = files.javaFiles
        assertEquals(1, javaFiles.size)
        assertEquals(0, files.jarFiles.size)
        val classNames = getSimpleFileNames(javaFiles)
        assertEquals(EXPECTED_SINGLE_FILE_NAME, classNames)
    }

    private fun getSimpleFileNames(fileNames: List<String>): Set<String> {
        return fileNames.stream()
            .map(Paths::get)
            .map { obj -> obj.fileName }
            .map { obj -> obj.toString() }
            .collect(Collectors.toSet())
    }

    @Test
    fun shouldNotFindAnything() {
        val emptyFiles = findFiles(Paths.get("src/test/resources/org/radum/empty/empty2"))
        assertEquals(0, emptyFiles.javaFiles.size)
        assertEquals(0, emptyFiles.jarFiles.size)
    }

    @Test
    fun shouldFindJarFiles() {
        val files = findFiles(getComplexProjectPath())
        assertEquals(2, files.jarFiles.size)
    }

    companion object {
        private val EXPECTED_FILE_NAMES = setOf(
            "Empty.java",
            "Client.java",
            "DataObject.java",
            "ExtendedData.java",
            "IProvider.java",
            "Provider.java"
        )
        private val EXPECTED_SINGLE_FILE_NAME = setOf(
            "Empty.java"
        )
    }
}