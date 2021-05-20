package org.vladg.jafax.repository.model

import org.vladg.jafax.utils.GenericTypeService

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

    val allMethodCalls: Set<Method> by lazy {
        calledMethods.union(allContainedMethodCalls)
    }

    val allContainedMethodCalls: Set<Method> by lazy {
        containedClasses.flatMap { it.allMethodCalls }
                        .union(containedMethods.flatMap { it.allMethodCalls })
    }

    val allFieldAccesses: Set<Attribute> by lazy {
        accessedFields.union(containedClasses.flatMap { it.allFieldAccesses })
                      .union(containedMethods.flatMap { it.allFieldAccesses })
                      .union(calledMethods.filter { it.isAccessor }.mapNotNull { it.accessorField })
    }

    val attributesForATFD: Set<Attribute> by lazy {
        accessedFields.filter { it.isPublic() }
                      .union(containedClasses.flatMap { it.attributesForATFD })
                      .union(containedMethods.filter { !it.isConstructor }.flatMap { it.attributesForATFD })
                      .union(calledMethods.filter { it.isAccessor }.filter { it.isPublic() }.mapNotNull { it.accessorField })
    }

    val allContainedMethods: Set<Method> by lazy {
        containedMethods.union(containedClasses.flatMap { it.allContainedMethods })
                        .union(containedMethods.flatMap { it.allContainedMethods })
    }

    val allReturnTypes: List<Class> by lazy {
        allInternalMethodCallReturnTypes +
        allContainedMethods.flatMap { GenericTypeService.getPossibleTypes(it.returnType, this) }
    }

    private val allInternalMethodCallReturnTypes: List<Class> by lazy {
        allMethodCalls.filter { it.isInternal }
                      .filter { it !in allContainedMethods }
                      .flatMap { GenericTypeService.getPossibleTypes(it.returnType, this) }
    }

    abstract val allContainedAttributes: Set<Attribute>

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