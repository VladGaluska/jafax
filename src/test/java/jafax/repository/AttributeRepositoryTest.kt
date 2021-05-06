package jafax.repository

import org.junit.Before
import org.junit.Test
import org.vladg.jafax.ast.repository.indexed.ContainerIndexedAttributeRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import jafax.AttributeGenerator.saveAttribute

class AttributeRepositoryTest {

    @Before
    fun clearRepository() {
        ContainerIndexedAttributeRepository.clear()
    }

    @Test
    fun `should not fail upon no container`() {
        saveAttribute("asd")
    }

    @Test
    fun `should index to empty string on no container`() {
        saveAttribute("test")
        assertNotNull(ContainerIndexedAttributeRepository.findByParentKeyAndName("", "test"))
    }

    @Test
    fun `should not find with different container key`() {
        saveAttribute("test")
        assertNull(ContainerIndexedAttributeRepository.findByParentKeyAndName("test1", "test"))
    }

    @Test
    fun `should index attributes on same container key`() {
        saveAttribute("test1")
        saveAttribute("test2")
        ContainerIndexedAttributeRepository.findAllForContainerKey("")?.keys?.let {
            assertEquals(2, it.size)
        }
    }

    @Test
    fun `should index attributes on different container keys`() {
        saveAttribute("test1", "test1")
        saveAttribute("test2", "test2")
        ContainerIndexedAttributeRepository.findAllForContainerKey("test1")?.keys?.let {
            assertEquals(1, it.size)
        }
        ContainerIndexedAttributeRepository.findAllForContainerKey("test2")?.keys?.let {
            assertEquals(1, it.size)
        }
    }

    @Test
    fun `should not fail or name duplicate`() {
        saveAttribute("test1")
        saveAttribute("test1")
    }

}