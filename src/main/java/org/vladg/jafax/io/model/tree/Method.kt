package org.vladg.jafax.io.model.tree

import kotlinx.serialization.Serializable

@Serializable
class Method(
    var id: Long,
    var signature: String,
    var callers: Set<Long>,
    var calledMethods: Set<Long>
)