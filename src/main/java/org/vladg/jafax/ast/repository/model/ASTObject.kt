package org.vladg.jafax.ast.repository.model

abstract class ASTObject(
    var name: String,
    var modifiers: Set<Modifier>
) {

    var id: Long = lastId++

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ASTObject -> other.id == id
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