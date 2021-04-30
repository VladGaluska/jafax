package org.vladg.jafax.ast.repository.model

class Attribute(
    name: String,
    val type: Class?,
    modifiers: Set<Modifier>,
    val container: Container?,
    val kind: AttributeKind
): ASTObject(name, modifiers) {

    enum class AttributeKind {
        Parameter, LocalVariable, Field
    }

    fun containerIdentifier(): String = container?.uniqueContainerIdentifier() ?: ""

    override fun toString(): String {
        return "Field $name"
    }
}
