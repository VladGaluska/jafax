package org.vladg.jafax.io.model

data class Relations(
        val source: String,
        val target: String,
        var extCalls: Int = 0,
        var extData: Int = 0,
        var hierarchy: Int = 0,
        var returns: Int = 0,
        var declarations: Int = 0,
        var extDataStrict: Int = 0,
        var all: Int = 0
)