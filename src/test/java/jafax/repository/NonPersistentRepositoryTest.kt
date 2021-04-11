package jafax.repository

import com.google.inject.Inject
import org.junit.Test
import org.vladg.ast.repository.NonPersistentRepository
import org.vladg.ast.repository.model.Class
import org.vladg.ast.repository.model.File
import org.vladg.ast.repository.model.Method
import org.junit.Assert.*

class NonPersistentRepositoryTest {

    @Inject
    private lateinit var fileRepository: NonPersistentRepository<File>

    @Inject
    private lateinit var classRepository: NonPersistentRepository<Class>

    @Inject
    private lateinit var methodRepository: NonPersistentRepository<Method>

    @Test
    fun `should increase index on new object save`() {
        populateRepositories()
    }

    private fun populateRepositories() {
        val file1 = File("File1")
        fileRepository.addObject(file1)
        val clazz = Class("Class", file1)
        classRepository.addObject(clazz)
        val method = Method("Method")
        methodRepository.addObject(method)
        val file2 = File("File2")
        fileRepository.addObject(file2)
    }
}