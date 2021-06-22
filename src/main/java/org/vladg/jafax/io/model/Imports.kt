package org.vladg.jafax.io.model

import kotlinx.serialization.Serializable

@Serializable
data class Imports(
    var file: String = "",
    var import: String = "",
    var static: Boolean = false,
    var onDemand: Boolean = false
)
