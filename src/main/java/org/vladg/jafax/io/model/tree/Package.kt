package org.vladg.jafax.io.model.tree

import kotlinx.serialization.Serializable

@Serializable
class Package(
    var id: Long,
    var name: String,
    var classes: Set<TreeClass>
)