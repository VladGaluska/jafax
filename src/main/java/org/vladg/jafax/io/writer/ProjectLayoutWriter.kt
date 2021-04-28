package org.vladg.jafax.io.writer

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.vladg.jafax.ast.repository.model.*
import java.nio.file.Path

class ProjectLayoutWriter {

    private val module = SerializersModule {
        polymorphic(ASTObject::class) {
            subclass(Class::class)
            subclass(Method::class)
            subclass(Attribute::class)
        }
        polymorphic(Container::class) {
            subclass(Class::class)
            subclass(Method::class)
        }
    }

    private val format = Json {
        serializersModule = module
        prettyPrint = true
        encodeDefaults = true
    }

    fun writeClasses(path: Path) {
        //file.writeText(format.encodeToString(this.commonRepository.findAll()))
    }

}