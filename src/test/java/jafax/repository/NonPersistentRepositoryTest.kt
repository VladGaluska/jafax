package jafax.repository

import com.google.inject.Inject
import org.junit.Test
import org.vladg.ast.repository.NonPersistentRepository
import org.vladg.ast.repository.model.Class
import org.vladg.ast.repository.model.File
import org.vladg.ast.repository.model.Method
import kotlin.test.*

class NonPersistentRepositoryTest {

    private val fileRepository: NonPersistentRepository<File> = NonPersistentRepository()

    private val classRepository: NonPersistentRepository<Class> = NonPersistentRepository()

    private val methodRepository: NonPersistentRepository<Method> = NonPersistentRepository()

    @Test
    fun `should increase index on new object save`() {
        val file1 = File("File1")
        val file2 = File("File2")
        assertEquals(file1.id + 1, file2.id)
    }

    @Test
    fun `should find proper container`() {
        populateRepositories()
        assertNotNull(NonPersistentRepository.popUntilTypeAnd<File> { it.name == "File1" })
    }

    @Test
    fun `container stack should be empty upon none found`() {
        populateRepositories()
        NonPersistentRepository.popUntilTypeAnd<Class> { it.name == "Not found" }
        assertTrue { NonPersistentRepository.containersSaved.empty() }
    }

    @Test
    fun `should not fail upon container not found in stack`() {
        populateRepositories()
        assertNull(NonPersistentRepository.popUntilTypeAnd<Class> { it.name == "Not found" })
    }

    @Test
    fun `should not find container that is not of instance`() {
        populateRepositories()
        assertNull(NonPersistentRepository.popUntilTypeAnd<File> { it.name == "Class" })
    }

    @Test
    fun `should not pop found container`() {
        populateRepositories()
        NonPersistentRepository.popUntilTypeAnd<File> { it.name == "File1" }
        assertNotNull(NonPersistentRepository.popUntilTypeAnd<File> { it.name == "File1" })
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