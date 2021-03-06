package org.vladg.jafax.io.serializers.decoder

import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.repository.model.ASTObject

object AstDecoder {

    private val linkUpdater = LinkUpdater()

    fun addObjectOrAddForUpdate(objectId: Long, adder: (ASTObject) -> Unit) {
        val obj = CommonRepository.findByIndex(objectId)
        if (obj != null) {
            adder(obj)
        } else {
            linkUpdater.addForUpdate(objectId, adder)
        }
    }

    fun addObjectsOrAddForUpdate(ids: List<Long>, adder: (ASTObject) -> Unit) {
        ids.forEach { this.addObjectOrAddForUpdate(it, adder) }
    }

    fun doUpdate(obj: ASTObject) {
        this.linkUpdater.update(obj)
    }

}