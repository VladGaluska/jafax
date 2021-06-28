package org.vladg.jafax.experimental

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.vladg.jafax.export.ChronosConcern
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.utils.extensions.logger
import java.nio.file.Path

@OptIn(ExperimentalSerializationApi::class)
object InterfaceComputer {
    private val logger = logger()

    val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    fun computeImports(path: Path, projName: String) {
        logger.info("Beginning Interface calculation...")

        val interfaces = ClassRepository.topLevelClasses
            .filter { it.isInterface }
            .filterNot { it.fileName == null }
            .map { ChronosConcern(it.fileName!!, "jafax.interface") }

        val abstractClasses = ClassRepository.topLevelClasses
            .filter { !it.isInterface && it.isAbstract() }
            .filterNot { it.fileName == null }
            .map { ChronosConcern(it.fileName!!, "jafax.abstractclass") }

        path.resolve("$projName-interfaces.json").toFile().writeText(json.encodeToString(interfaces))
        path.resolve("$projName-abstract-classes.json").toFile().writeText(json.encodeToString(abstractClasses))

    }
}
