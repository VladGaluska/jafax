package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.Class

object ClassRepository {

    private val classes: MutableList<Class> = ArrayList()

    fun addClass(clazz: Class) {
        this.classes.add(clazz)
        CommonRepository.addObject(clazz)
    }

    fun getAll(): List<Class> =
        this.classes

    fun clear() = classes.clear()

}