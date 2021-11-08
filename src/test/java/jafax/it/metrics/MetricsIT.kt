package jafax.it.metrics

import jafax.getSimpleProjectPath
import org.vladg.jafax.io.model.Metrics
import org.vladg.jafax.io.scanner.ProjectScanner
import org.vladg.jafax.io.writer.MetricsWriter
import org.vladg.jafax.metrics.MetricsComputer
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.utils.extensions.getLayoutFile
import java.io.File
import java.nio.file.Path
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MetricsIT {

    private val projectPath = getSimpleProjectPath()

    @BeforeTest
    fun clearRepositories() {
        ClassRepository.clear()
        CommonRepository.clear()
    }

    @Test
    fun `should properly compute metrics when using the layout file`() {
        val layoutFile = getLayoutFile("Layout.JSON")
        layoutFile.delete()
        verifyMetrics()
    }

    @Test
    fun `should properly compute metrics when parsing with ast`() {
        verifyMetrics()
    }

    private fun verifyMetrics() {
        computeMetrics()
        `metrics should be the same`(
                MetricsWriter.readMetricsFromFile(getExpectedMetricsFile()),
                MetricsWriter.readMetricsFromFile(getActualMetricsFile(projectPath))
        )
    }

    private fun `metrics should be the same`(expectedMetrics: List<Metrics>, actualMetrics: List<Metrics>) {
        assertEquals(expectedMetrics.size, actualMetrics.size, "Expected: $expectedMetrics actual: $actualMetrics")
        expectedMetrics.onEach { metricsToCheck ->
            assertEquals(
                    metricsToCheck,
                    actualMetrics.find { it.file == metricsToCheck.file && it.type == metricsToCheck.type }
            )
        }
    }

    private fun computeMetrics() {
        ProjectScanner.beginScan(projectPath, "Layout.JSON")
        MetricsComputer.computeMetrics(projectPath, "org1")
    }

    private fun getActualMetricsFile(path: Path) =
            File("$path/org1-metrics.csv")

    private fun getExpectedMetricsFile() =
            File("src/test/resources/expectedMetrics.csv")

}
