package org.vladg.jafax.repository.model

abstract class Container(
    var typeParameters: MutableList<Class?> = ArrayList(),
    name: String,
    modifiers: Set<Modifier>,
    container: Container?
) : ASTObject(name, modifiers, container) {

    val containedClasses: MutableSet<Class> = HashSet()

    val containedMethods: MutableSet<Method> = HashSet()

    val calledMethods: MutableSet<Method> = HashSet()

    val accessedFields: MutableSet<Attribute> = HashSet()

    val allMethodCalls: MutableSet<Method> by lazy {
        calledMethods.apply {
            addAll(containedClasses.flatMap { it.allMethodCalls })
            addAll(containedMethods.flatMap { it.allMethodCalls })
        }
    }

    val allFieldAccesses: MutableSet<Attribute> by lazy {
        accessedFields.apply {
            addAll(containedClasses.flatMap { it.allFieldAccesses })
            addAll(containedMethods.flatMap { it.allFieldAccesses })
        }
    }

    fun addToContainedClasses(clazz: Class) = containedClasses.add(clazz)

    fun addToContainedMethods(method: Method) = containedMethods.add(method)

    fun addToCalledMethods(method: Method) = calledMethods.add(method)

    fun addToAccessedFields(attribute: Attribute) = accessedFields.add(attribute)

    fun addToTypeParameters(parameterType: Class) = typeParameters.add(parameterType)

    abstract fun addToContainedAttributes(attribute: Attribute)

    abstract fun uniqueContainerIdentifier(): String

    override fun isSame(value: ASTObject) =
            value is Container &&
            super.isSame(value) &&
            containedClasses == value.containedClasses &&
            containedMethods == value.containedMethods &&
            calledMethods == value.calledMethods &&
            accessedFields == value.accessedFields

}