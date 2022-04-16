package org.vladg.jafax.io.model.tree

import kotlinx.serialization.Serializable

@Serializable
class Project(
    var name: String,
    var packages: Set<Package>
)