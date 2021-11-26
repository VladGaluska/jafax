package jafax

import org.vladg.jafax.repository.model.container.Class
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
                superInterfaces = mutableSetOf(generateEmptyClass(), generateEmptyClass()),
                isExternal = true,
                isTypeParameter = true,
                typeParameters = mutableListOf(generateEmptyClass()),
                parameterInstances = mutableSetOf(generateEmptyClass())
            )
        )

}