package org.vladg.jafax.repository.model.connection

import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.id.IdManager
import org.vladg.jafax.repository.model.container.Container
import org.vladg.jafax.repository.model.id.IdObject

abstract class Connection<T: Connectable>(var source: Container, var target: T, var obj: Attribute?) : IdObject {

    override var id = IdManager.lastId++

    override fun equals(other: Any?): Boolean =
        other is Connection<*> &&
        source.id == other.source.id &&
        target.id == other.target.id &&
        obj?.id == other.obj?.id

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + target.hashCode()
        result = 31 * result + (obj?.hashCode() ?: 0)
        result = 31 * result + id.hashCode()
        return result
    }

}