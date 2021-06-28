package org.vladg.jafax.imports

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.encodeToString
import org.vladg.jafax.io.model.Imports
import org.vladg.jafax.repository.FileRepository
import org.vladg.jafax.utils.extensions.logger
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.math.log

@OptIn(ExperimentalSerializationApi::class)
object ImportsComputer {

    private val logger = logger()

    val csv = Csv {
        hasHeaderRecord = true
        ignoreUnknownColumns = true
    }

    fun computeImports(path: Path, projName: String) {
        logger.info("Beginning imports calculation...")
        val allImports = FileRepository.findAll()
            .flatMap { file -> file.imports.map { Imports(file.name, it.importedClass, it.isStatic(), it.onDemand) } }

        path.resolve("$projName-imports.csv").toFile().writeText(csv.encodeToString(allImports))

    }
}
