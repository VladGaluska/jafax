package org.vladg.jafax.repository.model

import kotlinx.serialization.Transient
import org.vladg.jafax.repository.model.Attribute.AttributeKind

class Method(
    name: String,
    val signature: String,
    @Transient
    val key: String = "",
    val isConstructor: Boolean,
    modifiers: Set<Modifier>,
    container: Container?,
    val returnType: Class?
) : Container(name, modifiers, container) {

    val parameters: MutableSet<Attribute> = HashSet()

    val localVariables: MutableSet<Attribute> = HashSet()

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