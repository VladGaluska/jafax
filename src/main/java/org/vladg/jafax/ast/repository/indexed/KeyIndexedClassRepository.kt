package org.vladg.jafax.ast.repository.indexed

import org.vladg.jafax.repository.NonPersistentRepository
import org.vladg.jafax.repository.model.Class

object KeyIndexedClassRepository : NonPersistentRepository<Class>() {

    private val classesByKey: MutableMap<String, Class> = HashMap()

    override fun addObject(obj: Class) {
        classesByKey[obj.key] = obj
        super.addObject(obj)
    }

    fun findByKey(key: String): Class? {
        return classesByKey[key]
    }

}