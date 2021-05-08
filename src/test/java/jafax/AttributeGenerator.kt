package jafax

import org.vladg.jafax.ast.repository.indexed.ContainerIndexedAttributeRepository
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Container
import org.vladg.jafax.repository.model.Modifier

object AttributeGenerator {

    private fun getAttributeWithEmptyContainer(name: String) =
        getAttributeWithContainer(name, null)

    private fun getAttributeWithContainer(name: String, container: Container?) =
        Attribute(
            kind = Attribute.AttributeKind.Field,
            type = null,
            name = name,
            modifiers = setOf(),
            container = container
        )

    private fun getAttributeWithContainerKey(name: String, containerKey: String) =
        getAttributeWithContainer(
            name,
            Class(key = containerKey)
        )

    fun saveAttribute(name: String, containerKey: String? = null) =
        if (containerKey != null) saveAttribute(getAttributeWithContainerKey(name, containerKey))
        else saveAttribute(getAttributeWithEmptyContainer(name))

    private fun saveAttribute(attribute: Attribute) =
        ContainerIndexedAttributeRepository.addAttribute(
            attribute
        )

    fun generateEmptyAttribute(kind: Attribute.AttributeKind = Attribute.AttributeKind.Field) =
        Attribute(
            kind = kind
        )

    fun generateFilledAttribute() =
        Attribute(
            type = Class(),
            kind = Attribute.AttributeKind.Field,
            modifiers = setOf(Modifier.Protected, Modifier.Static),
            container = Class()
        )

}