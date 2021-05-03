package org.vladg.jafax.ast.repository.indexed

import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.repository.model.Attribute

object ContainerIndexedAttributeRepository {

    private val attributesByContainer: MutableMap<String, MutableMap<String, Attribute>> = HashMap()

    fun addAttribute(attribute: Attribute) {
        cacheAttributeByContainer(attribute)
        CommonRepository.addObject(attribute)
    }

    private fun cacheAttributeByContainer(attribute: Attribute) {
        val containerIdentifier = attribute.containerIdentifier()
        attributesByContainer
            .computeIfAbsent(containerIdentifier) { HashMap() }[attribute.name] = attribute
    }

    fun findByParentKeyAndName(key: String, name: String): Attribute? {
        return attributesByContainer[key]?.get(name)
    }

}