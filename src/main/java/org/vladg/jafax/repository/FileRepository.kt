package org.vladg.jafax.repository

import org.vladg.jafax.io.NamePrefixTrimmer
import org.vladg.jafax.repository.model.File

object FileRepository : NonPersistentRepository<String, File>() {

    lateinit var namePrefixTrimmer: NamePrefixTrimmer

    override fun objectIdentifier(obj: File): String = obj.name

    fun findOrCreate(name: String): File {
        val relativeName = namePrefixTrimmer.trimString(name)
        return if (objects.containsKey(relativeName)) objects[relativeName]!!
        else File(relativeName).also { addObject(it) }

    }
}
