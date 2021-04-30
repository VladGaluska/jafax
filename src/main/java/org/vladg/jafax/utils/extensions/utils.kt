package org.vladg.jafax.utils.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.vladg.jafax.ast.repository.model.ASTObject
import org.vladg.jafax.ast.repository.model.Class
import org.vladg.jafax.ast.repository.model.Modifier
import java.util.*
import java.util.stream.Stream

inline fun <reified T> T.logger(): Logger {
    if (T::class.isCompanion) {
        return LoggerFactory.getLogger(T::class.java.enclosingClass)
    }
    return LoggerFactory.getLogger(T::class.java)
}

fun <T> Array<T>.stream(): Stream<T> = Arrays.stream(this)

fun convertFilePathToUniversalPath(path: String): String = path.replace("\\", "/")

fun stringToModifiers(toDecode: List<String>): Set<Modifier> =
    toDecode.map { Modifier.valueOf(it) }.toSet()