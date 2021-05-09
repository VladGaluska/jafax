package org.vladg.jafax.ast.repository

import org.eclipse.jdt.core.dom.IBinding
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Container
import org.vladg.jafax.repository.model.Method
import org.vladg.jafax.utils.extensions.ast.bindingNameFromType
import java.util.*

object ContainerStack {

    private val containersSaved = Stack<Container>()

    private inline fun <reified T> popUntilContainerOfTypeAndKey(key: String): T? {
        while (!containersSaved.isEmpty()) {
            val container = containersSaved.peek()
            if (container is T && container.uniqueContainerIdentifier() == key) {
                return container
            }
            containersSaved.pop()
        }
        return null
    }

    fun popUntilBindingObject(binding: IBinding?): Container? {
        binding ?: return null
        return when (binding.kind) {
            IBinding.TYPE -> popUntilContainerOfTypeAndKey<Class>(binding.key)
            IBinding.METHOD -> popUntilContainerOfTypeAndKey<Method>(binding.key)
            else -> throw IllegalStateException("Unsupported parent type: " + bindingNameFromType(binding.kind))
        }
    }

    fun addToStack(container: Container) {
        containersSaved.push(container)
    }

    fun clearStack() {
        containersSaved.clear()
    }

}
