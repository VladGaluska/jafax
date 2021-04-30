package org.vladg.jafax.ast.repository

import org.vladg.jafax.ast.repository.model.ASTObject

class CommonRepository {

    private val objects: MutableList<ASTObject> = ArrayList()

    fun addObject(obj: ASTObject) =
        this.objects.add(obj)

    fun getAll(): List<ASTObject> = objects

}