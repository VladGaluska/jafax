package org.vladg.jafax.repository.model.connection

import org.vladg.jafax.repository.model.id.IdManager
import org.vladg.jafax.repository.model.container.Container
import org.vladg.jafax.repository.model.id.IdObject

abstract class Connection<T: Connectable>(var source: Container, var target: T) : IdObject {

    override var id = IdManager.lastId++

    override fun equals(other: Any?): Boolean =
        other is Access &&
        source.id == other.id &&
        target.id == other.id

}