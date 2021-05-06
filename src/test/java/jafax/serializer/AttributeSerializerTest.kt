package jafax.serializer

import jafax.AttributeGenerator.generateEmptyAttribute
import jafax.AttributeGenerator.generateFilledAttribute
import jafax.encodeAstObject
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AttributeSerializerTest {

    private val ignorableProperties = listOf(
        "class",
        "modifiers",
        "container"
    )

    @Test
    fun `should not serialize ignorable properties when values are default`() {
        val serializedAttribute = encodeAstObject(generateEmptyAttribute())
        ignorableProperties.forEach {
            assertFalse(serializedAttribute.containsKey(it))
        }
    }

    @Test
    fun `should serialize ignorable properties when values are not default`() {
        val serializedAttribute = encodeAstObject(generateFilledAttribute())
        ignorableProperties.forEach {
            assertTrue(serializedAttribute.containsKey(it))
        }
    }

}