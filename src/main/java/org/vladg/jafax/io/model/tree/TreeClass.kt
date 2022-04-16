package org.vladg.jafax.io.model.tree

import kotlinx.serialization.Serializable

@Serializable
class TreeClass(
    var id: Long,
    var name: String,
    var methods: Set<Method>,
    var dependencies: Set<String>
)