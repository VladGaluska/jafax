package org.vladg.ast.repository

import org.vladg.ast.repository.model.ASTObject
import org.vladg.ast.repository.NonPersistentRepository
import org.vladg.ast.repository.model.Container
import java.util.*

class NonPersistentRepository<T : ASTObject?> {

    private val objects: MutableMap<Long, T> = HashMap()

    fun addObject(obj: T) {
        if (obj is Container) {
            containersSaved.push(obj as Container)
        }
        objects[obj!!.id] = obj
    }

    fun findById(id: Long): T? {
        return objects[id]
    }

    companion object {

        val containersSaved = Stack<Container>()

        inline fun <reified T : Container> popUntilTypeAnd(accept: (container: T) -> Boolean): T? {
            while (!containersSaved.isEmpty()) {
                val container = containersSaved.peek()
                if (container is T && accept(container)) {
                    return container
                }
                containersSaved.pop()
            }
            return null
        }

    }
}