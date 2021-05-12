package jafax.it.layout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Modifier
import kotlin.test.assertEquals

@Serializable
@SerialName("MockAttribute")
class MockAttribute(
    var name: String,
    var kind: String,
    var typeName: String,
    var modifiers: Set<String> = HashSet(),
    var containerName: String? = null
): Mock() {

    fun verify(attribute: Attribute) {
        assertEquals(name, attribute.name)
        assertEquals(Attribute.AttributeKind.valueOf(kind), attribute.kind)
        assertEquals(typeName, attribute.type?.name)
        assertEquals(modifiers.map { Modifier.valueOf(it) }.toSet(), attribute.modifiers)
        assertEquals(containerName, attribute.container?.name)
    }

    override fun isSame(obj: ASTObject) =
            name == obj.name && containerName == obj.container?.name


}