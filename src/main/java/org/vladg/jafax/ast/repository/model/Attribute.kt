package org.vladg.jafax.ast.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.vladg.jafax.io.ASTPropertySerializer

@Serializable
@SerialName("Attribute")
class Attribute(
    val name: String,
    @Serializable(with = ASTPropertySerializer::class)
    @SerialName("class")
    val type: Class?,
    val modifiers: Set<String>,
    @Serializable(with = ASTPropertySerializer::class)
    val container: Container?,
    val kind: AttributeKind
): ASTObject() {

    enum class AttributeKind {
        Parameter, LocalVariable, Field
    }

    fun containerIdentifier(): String = container?.uniqueContainerIdentifier() ?: ""

    override fun toString(): String {
        return "Field $name"
    }
}
