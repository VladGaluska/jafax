package jafax

import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Modifier

object ClassGenerator {

    fun generateEmptyClass() =
        Class()

    fun generateFilledClass() =
        fillContainer(
            Class(
                isInterface = true,
                modifiers = setOf(Modifier.Static),
                container = generateEmptyClass(),
                superClass = generateEmptyClass(),
                superInterfaces = mutableSetOf(generateEmptyClass()),
                isExternal = true
            )
        )

}