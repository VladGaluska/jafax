package org.vladg.jafax.ast.repository.indexed

import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.NonPersistentRepository
import org.vladg.jafax.repository.model.Class

object KeyIndexedClassRepository : NonPersistentRepository<String, Class>() {

    override fun addObject(obj: Class) {
        objects[objectIdentifier(obj)] = obj
        ClassRepository.addClass(obj)
    }

    override fun objectIdentifier(obj: Class): String =
        obj.key

}