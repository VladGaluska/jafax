package org.vladg.jafax.utils.filefinder

data class FoundFiles (val javaFiles: List<String> = ArrayList(),
                       val jarFiles: List<String> = ArrayList())