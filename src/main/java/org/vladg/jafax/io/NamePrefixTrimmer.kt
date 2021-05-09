package org.vladg.jafax.io

object NamePrefixTrimmer {

    private var commonPrefix: String? = null

    fun registerName(name: String) {
        commonPrefix =
                commonPrefix?.commonPrefixWith(name) ?: name
    }

    fun trimString(value: String) =
            value.replaceFirst(commonPrefix ?: "", "")

}