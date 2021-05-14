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

    fun isProtected() = modifiers.contains(Modifier.Protected)

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

    open fun isInternal(): Boolean =
            container?.isInternal() ?: false

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