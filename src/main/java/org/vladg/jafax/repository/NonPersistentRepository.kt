package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.ASTObject

open class NonPersistentRepository<T : ASTObject?> {

    private val objects: MutableMap<Long, T> = HashMap()

    open fun addObject(obj: T) {
        objects[obj!!.id] = obj
        CommonRepository.addObject(obj)
    }

    fun findById(id: Long): T? {
        return objects[id]
    }

    fun findAll(): MutableCollection<T> {
        return objects.values
    }

}