package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.container.Class

object ClassRepository {

    private val classes: MutableList<Class> = ArrayList()

    fun addClass(clazz: Class) {
        this.classes.add(clazz)
        CommonRepository.addObject(clazz)
    }

    fun getAll(): List<Class> =
        this.classes

    val topLevelClasses: List<Class>
        get() = this.classes.filter { it.container == null && it.isInternal && it.fileName != null }

    fun clear() = classes.clear()

}