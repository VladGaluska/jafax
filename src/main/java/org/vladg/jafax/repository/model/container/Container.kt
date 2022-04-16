package org.vladg.jafax.repository.model.container

import org.vladg.jafax.ast.connection.AccessHandler
import org.vladg.jafax.ast.connection.InvocationHandler
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Modifier
import org.vladg.jafax.repository.model.connection.Access
import org.vladg.jafax.repository.model.connection.Invocation
import org.vladg.jafax.utils.GenericTypeService

abstract class Container(
        var typeParameters: MutableList<Class?> = ArrayList(),
        name: String,
        modifiers: Set<Modifier>,
        container: Container?
) : ASTObject(name, modifiers, container) {

    val containedClasses: MutableSet<Class> = HashSet()

    val containedMethods: MutableSet<Method> = HashSet()

    val invocations: MutableSet<Invocation> = HashSet()

    val accesses: MutableSet<Access> = HashSet()

    val allInvocations: Set<Invocation> by lazy {
        invocations.union(allContainerInvocations)
    }

    val allContainerInvocations: Set<Invocation> by lazy {
        containedClasses.flatMap { it.allInvocations }
                        .union(containedMethods.flatMap { it.allInvocations })
    }

    val allFieldAccesses: Set<Access> by lazy {
        accesses.union(containedClasses.flatMap { it.allFieldAccesses })
                .union(containedMethods.flatMap { it.allFieldAccesses })
                .union(accessesFromInvocations)
    }

    val attributesForATFD: Set<Access> by lazy {
        accesses.filter { it.target.isPublic() }
                .union(containedClasses.flatMap { it.attributesForATFD })
                .union(containedMethods.filter { !it.isConstructor }.flatMap { it.attributesForATFD })
                .union(accessesFromInvocations.filter { it.target.isPublic() })
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
        allInvocations.map { it.target }
                      .filter { it.isInternal }
                      .filter { it !in allContainedMethods }
                      .flatMap { GenericTypeService.getPossibleTypes(it.returnType, this) }
    }

    private val accessesFromInvocations: Collection<Access> by lazy {
        invocations.filter { it.target.isAccessor }
                   .filter { it.target.accessorField != null }
                   .map{ AccessHandler.findOrCreateAccess(this, it.target.accessorField!!, it.obj) }
    }

    abstract val allContainedAttributes: Set<Attribute>

    fun addToContainedClasses(clazz: Class) = containedClasses.add(clazz)

    fun addToContainedMethods(method: Method) = containedMethods.add(method)

    fun addToInvocations(method: Method, obj: Attribute?) =
        InvocationHandler.add(this, method, obj)
            .also { this.invocations.add(it) }

    fun addToAccesses(attribute: Attribute, obj: Attribute?) =
        AccessHandler.add(this, attribute, obj)
            .also { this.accesses.add(it) }

    fun addToTypeParameters(parameterType: Class) = typeParameters.add(parameterType)

    abstract fun addToContainedAttributes(attribute: Attribute)

    abstract fun uniqueContainerIdentifier(): String

    override fun isSame(value: ASTObject) =
            value is Container &&
            super.isSame(value) &&
            containedClasses == value.containedClasses &&
            containedMethods == value.containedMethods &&
            invocations == value.invocations &&
            accesses == value.accesses

}