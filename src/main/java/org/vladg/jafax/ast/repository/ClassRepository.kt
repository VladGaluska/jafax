package org.vladg.jafax.ast.repository

import org.vladg.jafax.ast.repository.model.Class

class ClassRepository : NonPersistentRepository<Class>() {

    private val classesByKey: MutableMap<String, Class> = HashMap()

    override fun addObject(obj: Class) {
        classesByKey[obj.key] = obj
        super.addObject(obj)
    }

    fun findByKey(key: String): Class? {
        return this.classesByKey[key]
    }

}