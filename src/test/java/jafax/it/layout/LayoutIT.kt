package jafax.it.layout

import jafax.getSimpleProjectPath
import kotlinx.serialization.decodeFromString
import org.junit.Test
import org.vladg.jafax.io.scanner.ProjectScanner
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.utils.extensions.getLayoutFile
import java.io.File
import kotlin.test.BeforeTest

class LayoutIT {

    private val projectPath = getSimpleProjectPath()

    @BeforeTest
    fun clearRepositories() {
        ClassRepository.clear()
        CommonRepository.clear()
    }

    @Test
    fun `should properly extract data without file`() {
        val layoutFile = getLayoutFile("Layout.JSON")
        layoutFile.delete()
        scanAndVerify()
    }

    @Test
    fun `should properly extract data with file`() {
        scanAndVerify()
    }

    private fun scanAndVerify() {
        ProjectScanner.beginScan(projectPath, "Layout.JSON")
        verifyLayout(getExpectedLayout())
    }

    private fun verifyLayout(mockLayout: List<Mock>) {
        mockLayout.forEach {
            when(it) {
                is MockClass -> {
                    it.verify(findForMock(it))
                }
                is MockMethod -> {
                    it.verify(findForMock(it))
                }
                is MockAttribute -> {
                    it.verify(findForMock(it))
                }
            }
        }
    }

    private inline fun <reified T> findForMock(mock: Mock) =
            CommonRepository.findByFilter {
                it is T && mock.isSame(it)
            }.first() as T

    private fun getExpectedLayout() =
        MockFormat.format.decodeFromString<List<Mock>>(File("src/test/resources/expectedLayout.JSON").readText())

}
