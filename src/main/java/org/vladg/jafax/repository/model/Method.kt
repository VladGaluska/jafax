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
    var isDefaultConstructor: Boolean = false,
    typeParameters: MutableList<Class?> = ArrayList(),
    name: String = "",
    modifiers: Set<Modifier> = HashSet(),
    container: Container? = null
) : Container(typeParameters, name, modifiers, container) {

    override val allContainedAttributes: Set<Attribute> by lazy {
        parameters.union(localVariables)
                  .union(containedMethods.flatMap { it.allContainedAttributes })
                  .union(containedClasses.flatMap { it.allContainedAttributes })
    }

    val parameters: MutableSet<Attribute> = HashSet()

    val localVariables: MutableSet<Attribute> = HashSet()

    val accessorField: Attribute? by lazy {
        if (isAccessor) {
            topLevelClass?.getFieldByName(fieldNameFromAccessorMethod(this.name))
        } else {
            null
        }
    }

    private fun fieldNameFromAccessorMethod(name: String) =
            name.replaceFirst("get", "", true)
                .replaceFirst("set", "", true)
                .replace(Regex("\\([a-zA-Z, ]*\\)"), "")

    val isAccessor: Boolean by lazy {
        isInternal &&
        (isGetter() || isSetter()) &&
        cyclomaticComplexity == 1 &&
        calledMethods.size == 0 &&
        hasAtMostOneAccessedAttributeOfSameClass()
    }

    val totalCyclomaticComplexity: Int by lazy {
        cyclomaticComplexity +
        containedMethods.map { it.totalCyclomaticComplexity }.sum() +
        containedClasses.map { it.totalCyclomaticComplexity }.sum()
    }

    fun incrementComplexity() {
        cyclomaticComplexity ++
    }

    private fun hasAtMostOneAccessedAttributeOfSameClass() =
         accessedFields.size == 0 ||
        (accessedFields.size == 1 && accessedFields.first().firstContainerClass == this.firstContainerClass)

    private fun isGetter(): Boolean =
        name.startsWith("get", true) &&
        parameters.size == 0 &&
        localVariables.size == 0

    private fun isSetter(): Boolean =
        name.startsWith("set", true) &&
        parameters.size == 1 &&
        localVariables.size == 0

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