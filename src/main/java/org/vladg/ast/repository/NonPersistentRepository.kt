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
        private val containersSaved = Stack<Container>()
        fun popUntilInstance(toCheck: Class<out Container?>): Container? {
            while (!containersSaved.isEmpty()) {
                val container = containersSaved.pop()
                if (container.javaClass == toCheck) {
                    return container
                }
            }
            return null
        }
    }
}