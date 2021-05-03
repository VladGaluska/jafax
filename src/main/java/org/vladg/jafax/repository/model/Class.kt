package org.vladg.jafax.repository.model

import kotlinx.serialization.Serializable
import org.vladg.jafax.io.serializers.ClassSerializer

@Serializable(with = ClassSerializer::class)
class Class(
    name: String = "",
    var fileName: String? = null,
    var isInterface: Boolean = false,
    modifiers: Set<Modifier> = HashSet(),
    val key: String = "",
    container: Container?,
    var superClass: Class? = null,
    var superInterfaces: MutableSet<Class> = HashSet(),
    var isExternal: Boolean = false
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