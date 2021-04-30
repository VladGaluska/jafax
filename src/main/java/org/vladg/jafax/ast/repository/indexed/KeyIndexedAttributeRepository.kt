package org.vladg.jafax.ast.repository.indexed

import org.vladg.jafax.ast.repository.NonPersistentRepository
import org.vladg.jafax.ast.repository.model.Attribute

object KeyIndexedAttributeRepository : NonPersistentRepository<Attribute>() {

    private val attributesByContainer: MutableMap<String, MutableMap<String, Attribute>> = HashMap()

    fun addAttribute(attribute: Attribute) {
        cacheAttributeByContainer(attribute)
        super.addObject(attribute)
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