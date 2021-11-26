package org.vladg.jafax.ast.repository.indexed

import org.vladg.jafax.repository.NonPersistentRepository
import org.vladg.jafax.repository.model.container.Method

object KeyIndexedMethodRepository: NonPersistentRepository<String, Method>() {

    override fun objectIdentifier(obj: Method): String =
        obj.key

}