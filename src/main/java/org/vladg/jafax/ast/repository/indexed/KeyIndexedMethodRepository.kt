package org.vladg.jafax.ast.repository.indexed

import org.vladg.jafax.ast.repository.NonPersistentRepository
import org.vladg.jafax.ast.repository.model.Method

object KeyIndexedMethodRepository: NonPersistentRepository<Method>() {

    private val methodsByKey: MutableMap<String, Method> = HashMap()

    override fun addObject(obj: Method) {
        methodsByKey[obj.key] = obj
        super.addObject(obj)
    }

    fun findByKey(key: String): Method? {
        return methodsByKey[key]
    }

}