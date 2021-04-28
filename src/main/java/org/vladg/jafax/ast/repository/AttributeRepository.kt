package org.vladg.jafax.ast.repository

import org.vladg.jafax.ast.repository.model.Attribute

class AttributeRepository : NonPersistentRepository<Attribute>() {

    private val attributesByContainer: MutableMap<String, MutableMap<String, Attribute>> = HashMap()

    fun addAttribute(attribute: Attribute) {
        this.cacheAttributeByContainer(attribute)
        super.addObject(attribute)
    }

    private fun cacheAttributeByContainer(attribute: Attribute) {
        val containerIdentifier = attribute.containerIdentifier()
        this.attributesByContainer
            .computeIfAbsent(containerIdentifier) { HashMap() }[attribute.name] = attribute
    }

    fun findByParentKeyAndName(key: String, name: String): Attribute? {
        return this.attributesByContainer[key]?.get(name)
    }

}