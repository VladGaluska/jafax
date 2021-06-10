package org.vladg.jafax.utils.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.vladg.jafax.repository.model.Modifier
import java.io.File
import java.nio.file.Path
import java.util.*
import java.util.stream.Stream
import kotlin.math.roundToInt

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

fun getLayoutFile(name: String) =
    File("./results/$name-layout.json")

infix fun Int.doubleDiv(i: Int): Double = this / i.toDouble()

fun Double.roundToTwoDecimals() = (this * 100.0).roundToInt() / 100.0
