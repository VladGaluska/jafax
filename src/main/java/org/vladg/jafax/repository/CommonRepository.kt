package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.ASTObject

object CommonRepository : NonPersistentRepository<Long, ASTObject>() {

    override fun addObject(obj: ASTObject) {
        objects[obj.id] = obj
    }

    override fun objectIdentifier(obj: ASTObject): Long =
        obj.id

}