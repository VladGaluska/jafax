package jafax.it.relations

import jafax.getSimpleProjectPath
import org.vladg.jafax.io.model.Relations
import org.vladg.jafax.io.scanner.ProjectScanner
import org.vladg.jafax.io.writer.RelationsWriter
import org.vladg.jafax.relations.RelationsComputer
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.utils.extensions.getLayoutFile
import java.io.File
import java.nio.file.Path
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RelationsIT {

    private val projectPath = getSimpleProjectPath()

    @BeforeTest
    fun clearRepositories() {
        ClassRepository.clear()
        CommonRepository.clear()
    }

    @Test
    fun `should properly compute relations when using the layout file`() {
        val layoutFile = getLayoutFile("Layout.JSON")
        layoutFile.delete()
        verifyRelations()
    }

    @Test
    fun `should properly compute relations when parsing with ast`() {
        verifyRelations()
    }

    private fun verifyRelations() {
        computeRelations()
        `relations should be the same`(
                RelationsWriter.readRelationsFromFile(getExpectedRelationsFile()),
                RelationsWriter.readRelationsFromFile(getActualRelationsFile(projectPath))
        )
    }

    private fun `relations should be the same`(expectedRelations: List<Relations>, actualRelations: List<Relations>) {
        expectedRelations.onEach { relationsToCheck ->
            assertEquals(
                    relationsToCheck,
                    actualRelations.find { it.source == relationsToCheck.source && it.target == relationsToCheck.target }
            )
        }
    }

    private fun computeRelations() {
        ProjectScanner.beginScan(projectPath, "Layout.JSON")
        RelationsComputer.computeRelations(projectPath, "org1")
    }

    private fun getActualRelationsFile(path: Path) =
            File("$path/org1-relations.csv")

    private fun getExpectedRelationsFile() =
            File("src/test/resources/expectedRelations.csv")

}
