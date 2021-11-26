package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.id.IdObject

abstract class NonPersistentRepository<K, V : IdObject> {

    protected val objects: MutableMap<K, V> = HashMap()

    open fun addObject(obj: V) {
        objects[this.objectIdentifier(obj)] = obj
        CommonRepository.addObject(obj)
    }

    fun findByFilter(filter: (IdObject) -> Boolean) =
            objects.values.filter(filter)

    abstract fun objectIdentifier(obj: V): K

    fun findByIndex(index: K): V? {
        return objects[index]
    }

    fun findAll(): MutableCollection<V> {
        return objects.values
    }

    fun clear() = objects.clear()

}