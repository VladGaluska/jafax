package org.vladg.jafax.ast.repository.model

import kotlinx.serialization.Serializable
import org.vladg.jafax.io.ASTPropertySerializer

@Serializable
abstract class Container : ASTObject() {

    private val containedClasses: MutableSet<@Serializable(with = ASTPropertySerializer::class) Class> = HashSet()

    private val containedMethods: MutableSet<@Serializable(with = ASTPropertySerializer::class) Method> = HashSet()

    private val calledMethods: MutableSet<@Serializable(with = ASTPropertySerializer::class) Method> = HashSet()

    private val accessedFields: MutableSet<@Serializable(with = ASTPropertySerializer::class) Attribute> = HashSet()

    fun addToContainedClasses(clazz: Class) = this.containedClasses.add(clazz)

    fun addToContainedMethods(method: Method) = this.containedMethods.add(method)

    fun addToCalledMethods(method: Method) = this.calledMethods.add(method)

    fun addToAccessedFields(attribute: Attribute) = this.accessedFields.add(attribute)

    abstract fun addToContainedAttributes(attribute: Attribute)

    abstract fun uniqueContainerIdentifier(): String

}