package jafax

import org.vladg.jafax.repository.model.Method
import org.vladg.jafax.repository.model.Modifier

object MethodGenerator {

    fun generateEmptyMethod() =
        Method()

    fun generateFilledMethod() =
        fillContainer(
            Method(
                isConstructor = true,
                returnType = ClassGenerator.generateEmptyClass(),
                container = ClassGenerator.generateEmptyClass(),
                modifiers = setOf(Modifier.Static),
                typeParameters = mutableListOf(ClassGenerator.generateEmptyClass())
            )
        )

}