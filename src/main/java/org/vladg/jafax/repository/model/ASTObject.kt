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

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ASTObject -> other.id == id
            else -> false
        }
    }

    fun getTopLevelClass(): Class? =
            container?.getTopLevelClass() ?:
            if (this is Class) this
            else null


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