package jafax.serializer

import jafax.AttributeGenerator
import jafax.ClassGenerator
import jafax.MethodGenerator
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.repository.model.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class DeserializerTest {

    val format = LayoutFormat.format

    @BeforeTest
    fun clearRepositories() {
        ClassRepository.clear()
        CommonRepository.clear()
    }

    @Test
    fun `deserialized empty class should be the same as before serialization`() {
        verifyDeserialization {
            ClassGenerator.generateEmptyClass()
        }
    }

    @Test
    fun `deserialized filled class should be the same as before serialization`() {
        verifyDeserialization {
            ClassGenerator.generateFilledClass()
        }
    }

    @Test
    fun `deserialized empty attribute should be the same as before serialization`() {
        verifyDeserialization {
            AttributeGenerator.generateEmptyAttribute()
        }
    }

    @Test
    fun `deserialized filled attribute should be the same as before serialization`() {
        verifyDeserialization {
            AttributeGenerator.generateFilledAttribute()
        }
    }

    @Test
    fun `deserialized empty method should be the same as before serialization`() {
        verifyDeserialization {
            MethodGenerator.generateEmptyMethod()
        }
    }

    @Test
    fun `deserialized filled method should be the same as before serialization`() {
        verifyDeserialization {
            MethodGenerator.generateFilledMethod()
        }
    }

    private fun verifyDeserialization(valueSupplier: () -> ASTObject) {
        val value = valueSupplier()
        encodeAndDecode(value)
        assertTrue(value.isSame(CommonRepository.findByIndex(value.id)!!))
    }

    private fun encodeAndDecodeWithDependencies(value: Attribute) {
        simplyEncodeAndDecode(value)
        encodeAndDecode(value.type)
        encodeAndDecode(value.container)
    }

    private inline fun <reified T> encodeAndDecodeCollection(values: MutableSet<T>) =
            values.forEach { encodeAndDecode(it) }

    private fun encodeAndDecodeContainerWithDependencies(container: Container) {
        encodeAndDecodeCollection(container.accessedFields)
        encodeAndDecodeCollection(container.calledMethods)
        encodeAndDecodeCollection(container.containedClasses)
        encodeAndDecodeCollection(container.containedMethods)
    }

    private fun encodeAndDecodeWithDependencies(value: Method) {
        simplyEncodeAndDecode(value)
        encodeAndDecodeCollection(value.localVariables)
        encodeAndDecodeCollection(value.parameters)
        encodeAndDecode(value.returnType)
        encodeAndDecode(value.container)
        encodeAndDecodeContainerWithDependencies(value)
    }

    private fun encodeAndDecodeWithDependencies(value: Class) {
        simplyEncodeAndDecode(value)
        encodeAndDecodeCollection(value.containedFields)
        encodeAndDecodeCollection(value.superInterfaces)
        encodeAndDecode(value.superClass)
        encodeAndDecode(value.container)
        encodeAndDecodeContainerWithDependencies(value)
    }

    private inline fun <reified T> simplyEncodeAndDecode(value: T): T =
            format.decodeFromString(format.encodeToString(value))

    private inline fun <reified T> encodeAndDecode(value: T) {
        when (value) {
            is Class -> encodeAndDecodeWithDependencies(value)
            is Method -> encodeAndDecodeWithDependencies(value)
            is Attribute -> encodeAndDecodeWithDependencies(value)
        }
    }

}