package jafax.it.layout

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

object MockFormat {

    private val module = SerializersModule {
        polymorphic(Mock::class) {
            subclass(MockClass::class)
            subclass(MockMethod::class)
            subclass(MockAttribute::class)
        }
    }

    val format = Json {
        serializersModule = module
        prettyPrint = true
        encodeDefaults = true
    }

}