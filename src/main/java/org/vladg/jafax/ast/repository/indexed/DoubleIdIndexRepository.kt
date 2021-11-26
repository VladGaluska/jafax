package org.vladg.jafax.ast.repository.indexed

import org.vladg.jafax.repository.NonPersistentRepository
import org.vladg.jafax.repository.model.connection.Connectable
import org.vladg.jafax.repository.model.connection.Connection

abstract class DoubleIdIndexRepository<T : Connectable> : NonPersistentRepository<Pair<Number, Number>, Connection<T>>() {

    override fun objectIdentifier(obj: Connection<T>): Pair<Number, Number> {
        return Pair(
            obj.source.id,
            obj.target.id
        )
    }

}