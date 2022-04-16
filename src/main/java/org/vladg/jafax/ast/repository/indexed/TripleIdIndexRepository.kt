package org.vladg.jafax.ast.repository.indexed

import org.vladg.jafax.repository.NonPersistentRepository
import org.vladg.jafax.repository.model.connection.Connectable
import org.vladg.jafax.repository.model.connection.Connection

abstract class TripleIdIndexRepository<T : Connectable> : NonPersistentRepository<Triple<Number, Number, Number?>, Connection<T>>() {

    override fun objectIdentifier(obj: Connection<T>): Triple<Number, Number, Number?> {
        return Triple(
            obj.source.id,
            obj.target.id,
            obj.obj?.id
        )
    }

}