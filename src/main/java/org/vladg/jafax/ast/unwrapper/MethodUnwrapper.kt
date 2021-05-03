package org.vladg.jafax.ast.unwrapper

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.IMethodBinding
import org.vladg.jafax.ast.repository.ContainerStack
import org.vladg.jafax.ast.repository.indexed.KeyIndexedMethodRepository
import org.vladg.jafax.repository.model.Container
import org.vladg.jafax.repository.model.Method
import org.vladg.jafax.utils.extensions.ast.getParent
import org.vladg.jafax.utils.extensions.ast.modifierSet
import org.vladg.jafax.utils.extensions.ast.signature

class MethodUnwrapper {

    @Inject
    private lateinit var containerService: ContainerService

    @Inject
    private lateinit var classUnwrapper: ClassUnwrapper

    private fun addMethod(method: Method) {
        KeyIndexedMethodRepository.addObject(method)
    }

    private fun findByKey(key: String): Method? {
        return KeyIndexedMethodRepository.findByKey(key)
    }

    private fun createMethod(binding: IMethodBinding, containerSupplier: () -> Container?): Method {
        val method = Method(
            name = binding.name,
            signature = binding.signature(),
            key = binding.key,
            isConstructor = binding.isConstructor,
            modifiers = binding.modifierSet(),
            container = containerSupplier(),
            returnType = classUnwrapper.findOrCreateClassForBinding(binding.returnType)
        )
        setMethodToItsContainer(method)
        return method
    }

    private fun setMethodToItsContainer(method: Method) =
        method.container?.addToContainedMethods(method)

    private fun createAndSaveMethod(binding: IMethodBinding, containerSupplier: () -> Container?): Method {
        val method = createMethod(binding, containerSupplier)
        addMethod(method)
        return method
    }

    fun findOrCreateMethodForBinding(binding: IMethodBinding?, useStack: Boolean = false): Method? {
        binding ?: return null
        return findByKey(binding.key)
            ?: createAndSaveMethod(binding) {
                if (!useStack) containerService.getOrCreateContainerForBinding(binding.getParent())
                else ContainerStack.popUntilBindingObject(binding.getParent())
            }
    }

}