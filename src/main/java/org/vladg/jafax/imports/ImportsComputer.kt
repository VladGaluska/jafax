package org.vladg.jafax.imports

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.encodeToString
import org.vladg.jafax.io.model.Imports
import org.vladg.jafax.repository.FileRepository
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi

@OptIn(ExperimentalSerializationApi::class)
object ImportsComputer {
    val csv = Csv {
        hasHeaderRecord = true
        ignoreUnknownColumns = true
    }

    fun computeImports(path: Path, projName: String) {
        val allImports = FileRepository.findAll()
            .flatMap { file -> file.imports.map { Imports(file.name, it.importedClass, it.isStatic(), it.onDemand) } }

        path.resolve("$projName-imports.csv").toFile().writeText(csv.encodeToString(allImports))

    }
}
