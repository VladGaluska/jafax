package org.vladg.jafax.ast.connection

import org.vladg.jafax.ast.repository.indexed.InvocationRepository
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.connection.Invocation
import org.vladg.jafax.repository.model.container.Container
import org.vladg.jafax.repository.model.container.Method

object InvocationHandler {

    fun add(source: Container, target: Method, obj: Attribute?): Invocation {
        var invocation = InvocationRepository.findByIndex(Triple(source.id, target.id, obj?.id))
        if (invocation == null) {
            invocation = Invocation(source, target)
            InvocationRepository.addObject(invocation)
        }
        return invocation as Invocation
    }

}