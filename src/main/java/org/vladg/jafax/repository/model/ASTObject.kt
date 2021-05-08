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

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ASTObject -> other.id == id
            else -> false
        }
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