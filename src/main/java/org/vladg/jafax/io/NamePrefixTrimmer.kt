package org.vladg.jafax.io

object NamePrefixTrimmer {

    private lateinit var commonPrefix: String

    fun registerName(name: String) {
        commonPrefix = commonPrefix.commonPrefixWith(name)
    }

    fun trimString(value: String): String {
        if (!commonPrefix.endsWith("/")) {
            val lastIndex = value.lastIndexOf("/")
            commonPrefix = commonPrefix.substring(0, lastIndex)
        }
        return value.replaceFirst(commonPrefix, "")
    }

}