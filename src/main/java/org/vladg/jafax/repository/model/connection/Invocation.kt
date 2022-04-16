package org.vladg.jafax.repository.model.connection

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.container.Container
import org.vladg.jafax.repository.model.container.Method

@JsonIdentityInfo(generator = ObjectIdGenerators.None::class, property = "id")
@JsonIdentityReference(alwaysAsId = true)
class Invocation(source: Container, target: Method, obj: Attribute? = null) : Connection<Method>(source, target, obj)