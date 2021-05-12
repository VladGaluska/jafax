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
    var cyclomaticComplexity: Int = 1,
    typeParameters: MutableList<Class?> = ArrayList(),
    name: String = "",
    modifiers: Set<Modifier> = HashSet(),
    container: Container? = null
) : Container(typeParameters, name, modifiers, container) {

    val parameters: MutableSet<Attribute> = HashSet()

    val localVariables: MutableSet<Attribute> = HashSet()

    fun incrementComplexity() {
        cyclomaticComplexity ++
    }

    override fun isSame(value: ASTObject) =
            value is Method &&
            super.isSame(value) &&
            signature == value.signature &&
            isConstructor == value.isConstructor &&
            returnType == value.returnType

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