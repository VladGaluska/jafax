package org.vladg.jafax.io.writer

import com.google.inject.Inject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.vladg.jafax.repository.model.*
import org.vladg.jafax.ast.unwrapper.ClassUnwrapper
import java.io.File
import java.nio.file.Path

class ProjectLayoutWriter {

    private val module = SerializersModule {
        polymorphic(ASTObject::class) {
            subclass(Class::class)
            /*subclass(Method::class)
            subclass(Attribute::class)*/
        }
        polymorphic(Container::class) {
            subclass(Class::class)
            /*subclass(Method::class)*/
        }
    }

    private val format = Json {
        serializersModule = module
        prettyPrint = true
        encodeDefaults = true
    }

    @Inject
    private lateinit var classUnwrapper: ClassUnwrapper

    fun writeClasses(path: Path) {
        val file = getLayoutFile(path)
        file.writeText(encodeObjects())
    }

    private fun encodeObjects(): String =
        format.encodeToString(this.classUnwrapper.findAll().toList())

    private fun getLayoutFile(path: Path) =
        File("$path/Layout.JSON")

}