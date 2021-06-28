package org.vladg.jafax.export

import kotlinx.serialization.Serializable

@Serializable
data class ChronosConcern(
    var entity: String,
    var tag: String,
    var strength: Int = 1
)
