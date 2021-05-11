package org.vladg.jafax.repository.model

import kotlinx.serialization.Serializable
import org.vladg.jafax.io.serializers.ClassSerializer

@Serializable(with = ClassSerializer::class)
open class Class(
    var fileName: String? = null,
    var isInterface: Boolean = false,
    val key: String = "",
    var superClass: Class? = null,
    var superInterfaces: MutableSet<Class> = HashSet(),
    var isExternal: Boolean = false,
    var parameterTypes: Any? = null,
    name: String = "",
    modifiers: Set<Modifier> = HashSet(),
    container: Container? = null
) : Container(name, modifiers, container) {

    val containedFields: MutableSet<Attribute> = HashSet()

    override fun addToContainedAttributes(attribute: Attribute) {
        containedFields.add(attribute)
    }

    override fun isInternal(): Boolean =
            container?.isInternal() ?: !isExternal

    fun addToInterfaces(clazz: Class) {
        superInterfaces.add(clazz)
    }

    override fun isSame(value: ASTObject) =
        value is Class &&
        super.isSame(value) &&
        fileName == value.fileName &&
        isInterface == value.isInterface &&
        superClass == value.superClass &&
        superInterfaces == value.superInterfaces &&
        isExternal == value.isExternal

    override fun uniqueContainerIdentifier(): String = key

    override fun toString(): String = "Class: $name"

}