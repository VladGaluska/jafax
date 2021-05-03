package org.vladg.jafax.repository.model

import kotlinx.serialization.Serializable
import org.vladg.jafax.io.serializers.ClassSerializer

@Serializable(with = ClassSerializer::class)
class Class(
    var fileName: String? = null,
    var isInterface: Boolean = false,
    val key: String = "",
    var superClass: Class? = null,
    var superInterfaces: MutableSet<Class> = HashSet(),
    var isExternal: Boolean = false,
    name: String = "",
    modifiers: Set<Modifier> = HashSet(),
    container: Container? = null
) : Container(name, modifiers, container) {

    val containedFields: MutableSet<Attribute> = HashSet()

    override fun addToContainedAttributes(attribute: Attribute) {
        containedFields.add(attribute)
    }

    fun addToInterfaces(clazz: Class) {
        superInterfaces.add(clazz)
    }

    override fun uniqueContainerIdentifier(): String = key

    override fun toString(): String = "Class: $name"

}