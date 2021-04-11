package org.vladg.jafax.utils.filefinder

import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

class FileVisitor : SimpleFileVisitor<Path?>() {

    private val javaMatcher: PathMatcher

    private val jarMatcher: PathMatcher

    val javaFiles: MutableList<String>

    val jarFiles: MutableList<String>

    init {
        javaFiles = ArrayList()
        jarFiles = ArrayList()
        javaMatcher = FileSystems.getDefault()
            .getPathMatcher("glob:*.java")
        jarMatcher = FileSystems.getDefault()
            .getPathMatcher("glob:*.jar")
    }


    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
        val name = file?.fileName
        if (name != null && attrs != null && attrs.isRegularFile) {
            if (javaMatcher.matches(name)) {
                javaFiles.add(file.toString())
            } else if (jarMatcher.matches(name)) {
                jarFiles.add(file.toString())
            }
        }
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
        exc?.printStackTrace()
        return FileVisitResult.CONTINUE
    }

}