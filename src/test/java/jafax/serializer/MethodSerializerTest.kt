package jafax.serializer

import jafax.MethodGenerator
import jafax.encodeAstObject
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MethodSerializerTest {

    private val ignorableProperties = listOf(
        "isConstructor",
        "returnType",
        "container",
        "modifiers",
        "parameters",
        "localVariables",
        "accessedFields",
        "containedClasses",
        "containedMethods",
        "calledMethods"
    )

    @Test
    fun `should not serialize ignorable properties when values are default`() {
        val serializedMethod = encodeAstObject(MethodGenerator.generateEmptyMethod())
        ignorableProperties.forEach {
            assertFalse(serializedMethod.containsKey(it))
        }
    }

    @Test
    fun `should serialize ignorable properties when values are not default`() {
        val serializedMethod = encodeAstObject(MethodGenerator.generateFilledMethod())
        ignorableProperties.forEach {
            assertTrue(serializedMethod.containsKey(it))
        }
    }

}