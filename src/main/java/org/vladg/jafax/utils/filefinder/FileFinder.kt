package org.vladg.jafax.utils.filefinder

import java.nio.file.Files
import java.nio.file.Path

object FileFinder {

    fun findFiles(path: Path): FoundFiles {
        val fileVisitor = FileVisitor()
        Files.walkFileTree(path, fileVisitor)
        return FoundFiles(fileVisitor.javaFiles, fileVisitor.jarFiles)
    }

}