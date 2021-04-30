package org.vladg.jafax.io.serializers.decoder

import org.vladg.jafax.ast.repository.model.ASTObject

class LinkUpdater {

    private val updatesById: MutableMap<Long, MutableList<(ASTObject) -> Unit>> = HashMap()

    fun addForUpdate(id: Long, updater: (ASTObject) -> Unit) =
        this.updatesById
            .computeIfAbsent(id) { ArrayList() }
            .add(updater)

    fun update(astObject: ASTObject) {
        this.updatesById[astObject.id]?.forEach { it(astObject) }
        this.updatesById.remove(astObject.id)
    }

}