package org.vladg.jafax.repository.model

import kotlinx.serialization.Serializable
import org.vladg.jafax.io.serializers.ClassSerializer

@Serializable(with = ClassSerializer::class)
class Class(
    var isInterface: Boolean = false,
    val key: String = "",
    var superClass: Class? = null,
    var superInterfaces: MutableSet<Class> = HashSet(),
    var isExternal: Boolean = false,
    var parameterInstances: MutableSet<Class?> = HashSet(),
    var isTypeParameter: Boolean = false,
    var pack: String = "",
    typeParameters: MutableList<Class?> = ArrayList(),
    name: String = "",
    modifiers: Set<Modifier> = HashSet(),
    container: Container? = null
) : Container(typeParameters, name, modifiers, container) {

    val fullyQualifiedName: String
        get() = if(pack.isNotBlank()) "$pack.$name" else name

    override var fileName: String? = null
        get() {
            if (field == null) {
                if (container != null) {
                    field = container?.fileName
                }
            }
            return field
        }

    private val cachedRelationToClasses: MutableMap<Class, Boolean> = HashMap()

    val protectedMembers: Set<ASTObject> by lazy {
        containedMethods.filter { it.isProtected() }
                .union(containedFields.filter { it.isProtected() })
                .toSet()
    }

    val DIT: Int by lazy {
        if (superClass?.isInternal == true) 1 + superClass!!.DIT
        else 0
    }

    val containedFields: MutableSet<Attribute> = HashSet()

    val functionalMethods: List<Method> by lazy {
        containedMethods.filter { it.isPublic() && !it.isAccessor && !it.isConstructor && !it.isAbstract() }
    }

    val allPublicMembers: List<ASTObject> by lazy {
        (containedMethods.filter { it.isPublic() && !it.isDefaultConstructor }
        + containedFields.filter { it.isPublic() })
    }

    val firstRealTypeSuperClass: Class? by lazy {
        if (superClass?.isFunctionalType == true) superClass
        else superInterfaces.find { it.isFunctionalType }
    }

    val totalCyclomaticComplexity: Int by lazy {
        containedMethods.filter { !it.isDefaultConstructor }
                        .map { it.totalCyclomaticComplexity }.sum() +
        containedClasses.map { it.totalCyclomaticComplexity }.sum()
    }

    val isParameterizableType: Boolean by lazy {
        typeParameters.filterNotNull().isNotEmpty()
    }

    private val isFunctionalType: Boolean by lazy {
        isInternal && !isTypeParameter
    }

    override val firstContainerClass: Class = this

    override val isInternal by lazy {
        container?.isInternal ?: !isExternal
    }

    override val allContainedAttributes: Set<Attribute> by lazy {
        containedFields.union(containedMethods.flatMap{ it.allContainedAttributes })
                       .union(containedClasses.flatMap { it.allContainedAttributes })
    }

    val overridingMethodsByClass: Map<Class, Set<Method>> by lazy {
        superInterfaces.union(setOf(superClass))
                .filterNotNull()
                .associateWith { containedMethods }
                .mapValues { it.value.filter { m -> it.key.hasMethodWithSignature(m.signature) }.toSet() }
    }

    val superClassOverridingMethods: Set<Method> by lazy {
        containedMethods.filter { superClass?.hasMethodWithSignature(it.signature) == true }
                .toSet()
    }

    fun addToParameterInstances(clazz: Class?) =
        parameterInstances.add(clazz)

    fun addToInterfaces(clazz: Class) =
        superInterfaces.add(clazz)

    fun getFieldByName(fieldName: String): Attribute? =
        containedFields.find { fieldName.toLowerCase().equals(it.name, true) }

    fun hasMethodWithSignature(signature: String): Boolean =
        containedMethods.find { it.signature == signature } != null

    fun isRelatedTo(clazz: Class): Boolean {
        if (clazz in cachedRelationToClasses) return cachedRelationToClasses[clazz]!!
        if (searchForClassInHierarchyTree(clazz)) return true
        cachedRelationToClasses[clazz] = false
        return false
    }

    private fun searchForClassInHierarchyTree(clazz: Class): Boolean {
        if (superClass?.isRelatedTo(clazz) == true ||
                superInterfaces.any { it.isRelatedTo(clazz) } ||
                this == clazz) {
            cachedRelationToClasses[clazz] = true
            return true
        }
        return false
    }

    override fun addToContainedAttributes(attribute: Attribute) {
        containedFields.add(attribute)
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
