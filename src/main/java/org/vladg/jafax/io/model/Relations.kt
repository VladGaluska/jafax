package org.vladg.jafax.io.model

data class Relations(
        val source: String,
        val target: String,
        val extCalls: Int,
        val extData: Int,
        val hierarchy: Int,
        val returns: Int,
        val declarations: Int,
        val extDataStrict: Int,
        val all: Int
)