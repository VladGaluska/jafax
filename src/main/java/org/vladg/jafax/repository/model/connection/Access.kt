package org.vladg.jafax.repository.model.connection

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.container.Container

@JsonIdentityInfo(generator = ObjectIdGenerators.None::class, property = "id")
@JsonIdentityReference(alwaysAsId = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Access(source: Container, target: Attribute) : Connection<Attribute>(source, target)