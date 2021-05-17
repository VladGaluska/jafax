package org.vladg.jafax.repository.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
abstract class ASTObject(
    var name: String,
    var modifiers: Set<Modifier>,
    @Contextual
    var container: Container?
) {

    var id: Long = lastId++

    open val fileName: String? by lazy { container?.fileName }

    open val firstContainerClass: Class? by lazy { container?.firstContainerClass }

    open val isInternal: Boolean by lazy { container?.isInternal ?: false }

    fun isChildOf(parent: ASTObject): Boolean =
            this == parent || container?.isChildOf(parent) == true

    fun isProtected() = Modifier.Protected in modifiers

    fun isPublic() = Modifier.Public in modifiers

    fun isAbstract() = Modifier.Abstract in modifiers

    fun isStatic() = Modifier.Static in modifiers

    fun isFinal() = Modifier.Final in modifiers

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ASTObject -> other.id == id
            else -> false
        }
    }

    open val topLevelClass: Class? by lazy {
        container?.topLevelClass
    }

    open val topLevelMethod: Method? by lazy {
        container?.topLevelMethod
    }

    open fun isSame(value: ASTObject) =
            name == value.name &&
            modifiers == value.modifiers &&
            container == value.container

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        private var lastId: Long = 0
    }
}