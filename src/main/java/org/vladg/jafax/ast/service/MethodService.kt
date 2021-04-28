package org.vladg.jafax.ast.service

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.IMethodBinding
import org.vladg.jafax.ast.repository.MethodRepository
import org.vladg.jafax.ast.repository.NonPersistentRepository
import org.vladg.jafax.ast.repository.model.Container
import org.vladg.jafax.ast.repository.model.Method
import org.vladg.jafax.utils.extensions.ast.getParent
import org.vladg.jafax.utils.extensions.ast.modifierSet
import org.vladg.jafax.utils.extensions.ast.signature

class MethodService {

    @Inject
    private lateinit var methodRepository: MethodRepository

    @Inject
    private lateinit var containerService: ContainerService

    @Inject
    private lateinit var classService: ClassService

    private fun addMethod(method: Method) {
        this.methodRepository.addObject(method)
    }

    private fun findByKey(key: String): Method? {
        return this.methodRepository.findByKey(key)
    }

    private fun createMethod(binding: IMethodBinding, containerSupplier: () -> Container?): Method {
        val method = Method(
            name = binding.name,
            signature = binding.signature(),
            key = binding.key,
            isConstructor = binding.isConstructor,
            modifiers = binding.modifierSet(),
            returnType = this.classService.findOrCreateClassForBinding(binding.returnType)
        )
        containerSupplier()?.addToContainedMethods(method)
        return method
    }

    private fun createAndSaveMethod(binding: IMethodBinding, containerSupplier: () -> Container?): Method {
        val method = this.createMethod(binding, containerSupplier)
        this.addMethod(method)
        return method
    }

    fun findOrCreateMethodForBinding(binding: IMethodBinding?, useStack: Boolean = false): Method? {
        binding ?: return null
        return this.findByKey(binding.key)
            ?: this.createAndSaveMethod(binding) {
                if (!useStack) this.containerService.getOrCreateContainerForBinding(binding.getParent())
                else NonPersistentRepository.popUntilBindingObject(binding.getParent())
            }
    }

}