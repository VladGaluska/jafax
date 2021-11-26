package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.id.IdObject

object CommonRepository : NonPersistentRepository<Long, IdObject>() {

    override fun addObject(obj: IdObject) {
        objects[obj.id] = obj
    }

    override fun objectIdentifier(obj: IdObject): Long =
        obj.id

}