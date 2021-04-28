package org.vladg.jafax.ast.repository.model

import kotlinx.serialization.Serializable

@Serializable
abstract class ASTObject {

    var id: Long = lastId++

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ASTObject -> other.id == this.id
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        private var lastId: Long = 0
    }

}