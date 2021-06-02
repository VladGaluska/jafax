package org.vladg.jafax.io

object NamePrefixTrimmer {

    private var commonPrefix: String? = null

    fun registerName(name: String) {
        if (commonPrefix == null) {
            commonPrefix = name
        }
        commonPrefix = commonPrefix!!.commonPrefixWith(name)
    }

    fun trimString(value: String): String {
        commonPrefix ?: return ""
        if (!commonPrefix!!.endsWith("/")) {
            val lastIndex = commonPrefix!!.lastIndexOf("/")
            commonPrefix = if (lastIndex == -1) ""
                           else commonPrefix!!.substring(0, lastIndex)
        }
        return value.replaceFirst(commonPrefix!!, "")
    }

}