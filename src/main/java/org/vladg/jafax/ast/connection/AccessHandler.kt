package org.vladg.jafax.ast.connection

import org.vladg.jafax.ast.repository.indexed.AccessRepository
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.connection.Access
import org.vladg.jafax.repository.model.container.Container

object AccessHandler {

    fun findOrCreateAccess(source: Container, target: Attribute, obj: Attribute?): Access =
        AccessRepository.findByIndex(Triple(source.id, target.id, obj?.id)) as Access? ?:
        createAccess(source, target)

    private fun createAccess(source: Container, target: Attribute): Access =
        Access(source, target).also { AccessRepository.addObject(it) }

    fun add(source: Container, target: Attribute, obj: Attribute?): Access {
        var access = AccessRepository.findByIndex(Triple(source.id, target.id, obj?.id))
        if (access == null) {
            access = Access(source, target)
            AccessRepository.addObject(access)
        }
        return access as Access
    }
}