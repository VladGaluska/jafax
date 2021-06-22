package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.File

object FileRepository : NonPersistentRepository<String, File>() {
    override fun objectIdentifier(obj: File): String = obj.name

    fun findOrCreate(name: String): File =
        if (objects.containsKey(name)) objects[name]!! else File(name).also { addObject(it) }
}
