package org.vladg.jafax.utils.filefinder

import java.nio.file.Files
import java.nio.file.Path

object FileFinder {

    fun findFiles(path: Path): JavaSourceFiles {
        val fileVisitor = FileVisitor()
        Files.walkFileTree(path, fileVisitor)
        return JavaSourceFiles(fileVisitor.javaFiles, fileVisitor.jarFiles)
    }

}