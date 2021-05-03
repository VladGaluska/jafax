package org.vladg.jafax.repository.model

class Attribute(
    name: String,
    val type: Class?,
    modifiers: Set<Modifier>,
    container: Container?,
    val kind: AttributeKind
): ASTObject(name, modifiers, container) {

    enum class AttributeKind {
        Parameter, LocalVariable, Field
    }

    fun containerIdentifier(): String = container?.uniqueContainerIdentifier() ?: ""

    override fun toString(): String {
        return "Field $name"
    }
}
