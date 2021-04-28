package org.vladg.jafax.ast.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.vladg.jafax.io.ASTPropertySerializer

@Serializable
@SerialName("Class")
class Class(
    val name: String,
    var fileName: String? = null,
    val isInterface: Boolean,
    val modifiers: Set<String>,
    @Transient
    val key: String = "",
    @Serializable(with = ASTPropertySerializer::class)
    val superClass: Class?,
    val superInterfaces: Set<@Serializable(with = ASTPropertySerializer::class) Class?>,
    val isFromSource: Boolean
) : Container() {

    val fields: MutableSet<@Serializable(with = ASTPropertySerializer::class) Attribute?> = HashSet()

    override fun addToContainedAttributes(attribute: Attribute) {
        this.fields.add(attribute)
    }

    override fun uniqueContainerIdentifier(): String = key

    override fun toString(): String = "Class: $name"

}