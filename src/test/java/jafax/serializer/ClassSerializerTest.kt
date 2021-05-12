package jafax.serializer

import jafax.ClassGenerator
import jafax.encodeAstObject
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClassSerializerTest {

    private val ignorableProperties = listOf(
        "isInterface",
        "modifiers",
        "container",
        "superClass",
        "interfaces",
        "isExternal",
        "containedClasses",
        "containedMethods",
        "calledMethods",
        "containedFields",
        "accessedFields",
        "containedClasses",
        "containedMethods",
        "calledMethods",
        "isTypeParameter",
        "parameterInstances",
        "typeParameters"
    )

    @Test
    fun `should not serialize ignorable properties when values are default`() {
        val serializedClass = encodeAstObject(ClassGenerator.generateEmptyClass())
        ignorableProperties.forEach {
            assertFalse(serializedClass.containsKey(it))
        }
    }

    @Test
    fun `should serialize ignorable properties when values are not default`() {
        val serializedClass = encodeAstObject(ClassGenerator.generateFilledClass())
        ignorableProperties.forEach {
            assertTrue(serializedClass.containsKey(it), "$it is not contained!")
        }
    }

}