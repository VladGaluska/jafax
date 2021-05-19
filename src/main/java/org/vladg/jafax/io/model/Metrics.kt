package org.vladg.jafax.io.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Metrics(
        var file: String = "",
        @SerialName("class")
        var type: String = "",
        var AMW: Double = 1.0,
        var WMC: Int = 0,
        var NOM: Int = 0,
        var NOPA: Int = 0,
        var NProtM: Int = 0,
        var ATFD: Int = 0,
        var ATFD2: Int = 0,
        var FDP: Int = 0,
        var WOC: Double = 1.0,
        var BOvR: Double = .0,
        var CC: Int = 0,
        var CM: Int = 0,
        var CINT: Int = 0,
        var CDISP: Double = .0,
        var BUR: Double = .0,
        var HIT: Int = 0,
        var DIT: Int = 0,
        var NOC: Int = 0,
        var RFC: Int = 0
) {
}