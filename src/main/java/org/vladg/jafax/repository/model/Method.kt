package org.vladg.jafax.repository.model

import kotlinx.serialization.Serializable
import org.vladg.jafax.io.serializers.MethodSerializer
import org.vladg.jafax.repository.model.Attribute.AttributeKind

@Serializable(with = MethodSerializer::class)
class Method(
    var signature: String = "",
    var key: String = "",
    var isConstructor: Boolean = false,
    var returnType: Class? = null,
    name: String = "",
    modifiers: Set<Modifier> = HashSet(),
    container: Container? = null
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