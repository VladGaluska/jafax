package org.vladg.jafax.repository.model

import kotlinx.serialization.Serializable
import org.vladg.jafax.io.serializers.AttributeSerializer

@Serializable(with = AttributeSerializer::class)
class Attribute(
    var kind: AttributeKind? = null,
    var type: Class? = null,
    name: String = "",
    modifiers: Set<Modifier> = HashSet(),
    container: Container? = null
): ASTObject(name, modifiers, container) {

    enum class AttributeKind {
        Parameter, LocalVariable, Field
    }

    fun containerIdentifier(): String = container?.uniqueContainerIdentifier() ?: ""

    override fun isSame(value: ASTObject) =
            value is Attribute &&
            super.isSame(value) &&
            kind == value.kind &&
            type == value.type

    override fun toString(): String {
        return "Attribute $name"
    }
}
