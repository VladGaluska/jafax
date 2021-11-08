package org.vladg.jafax.io

class NamePrefixTrimmer(
    private val commonPrefix: String = ""
) {

    fun trimString(value: String): String {
        return value.replaceFirst(commonPrefix, "")
    }
}
