package org.vladg.jafax.io

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.vladg.jafax.repository.model.*

object LayoutFormat {

    private val module = SerializersModule {
        polymorphic(ASTObject::class) {
            subclass(Class::class)
            subclass(Method::class)
            subclass(Attribute::class)
            subclass(File::class)
            subclass(ImportStatement::class)
        }
        polymorphic(Container::class) {
            subclass(Class::class)
            subclass(Method::class)
        }
    }

    val format = Json {
        serializersModule = module
        prettyPrint = true
        encodeDefaults = true
    }

}
