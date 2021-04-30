package org.vladg.jafax.ast.repository

import org.vladg.jafax.ast.repository.model.ASTObject

object CommonRepository {

    private val objects: MutableMap<Long, ASTObject> = HashMap()

    fun addObject(obj: ASTObject) {
        objects[obj.id] = obj
    }

    fun findById(id: Long): ASTObject? =
        objects[id]

    fun getAll(): List<ASTObject> =
        objects.values.toList()

}