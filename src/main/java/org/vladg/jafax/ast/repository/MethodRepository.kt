package org.vladg.jafax.ast.repository

import org.vladg.jafax.ast.repository.model.Method

class MethodRepository: NonPersistentRepository<Method>() {

    private val methodsByKey: MutableMap<String, Method> = HashMap()

    override fun addObject(obj: Method) {
        this.methodsByKey[obj.key] = obj
        super.addObject(obj)
    }

    fun findByKey(key: String): Method? {
        return this.methodsByKey[key]
    }

}