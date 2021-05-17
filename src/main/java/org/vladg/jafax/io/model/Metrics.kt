package org.vladg.jafax.io.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Metrics(
        var file: String = "",
        @SerialName("type")
        var type: String = "",
        var AMW: Double = .1,
        var WMC: Int = 0,
        var NOM: Int = 0,
        var NOPA: Int = 0,
        var NOAV: Int = 0,
        var NProtM: Int = 0,
        var ATFD: Int = 0,
        var ATFD2: Int = 0,
        var FDP: Int = 0,
        var TCC: Int = 0,
        var LAA: Int = 0,
        var WOC: Double = .1,
        var BOvR: Int = 0,
        var CC: Int = 0,
        var CM: Int = 0,
        var CINT: Int = 0,
        var CDISP: Int = 0,
        var BUR: Int = 0,
        var HIT: Int = 0,
        var DIT: Int = 0,
        var NOC: Int = 0,
        var RFC: Int = 0,
        var CBO: Int = 0,
        var CBO2: Int = 0,
        var WMC_STATIC: Int = 0,
        var NOM_STATIC: Int = 0,
        var FANOUT_STATIC: Int = 0,
        var FANOUT_CLS_STATIC: Int = 0,
        var FANIN_STATIC: Int = 0,
        var FANIN_CLS_STATIC: Int = 0
) {
}