package org.vladg.jafax.ast.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.vladg.jafax.io.ASTPropertySerializer
import org.vladg.jafax.ast.repository.model.Attribute.AttributeKind

@Serializable
@SerialName("Method")
class Method(
    val name: String,
    val signature: String,
    @Transient
    val key: String = "",
    val isConstructor: Boolean,
    val modifiers: Set<String>,
    @Serializable(with = ASTPropertySerializer::class)
    val returnType: Class?
) : Container() {

    val parameters: MutableSet<@Serializable(with = ASTPropertySerializer::class) Attribute> = HashSet()

    val localVariables: MutableSet<@Serializable(with = ASTPropertySerializer::class) Attribute> = HashSet()

    override fun addToContainedAttributes(attribute: Attribute) {
        when(attribute.kind) {
            AttributeKind.Parameter -> parameters.add(attribute)
            AttributeKind.LocalVariable -> localVariables.add(attribute)
            else -> throw IllegalStateException("Methods cannot have attribute kind: ${attribute.kind}")
        }
    }

    override fun uniqueContainerIdentifier(): String = key

    override fun toString(): String = "Method $signature"

}