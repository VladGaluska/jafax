package org.vladg.jafax.ast.unwrapper

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.ASTNode
import org.eclipse.jdt.core.dom.IBinding
import org.eclipse.jdt.core.dom.IMethodBinding
import org.vladg.jafax.ast.repository.ContainerStack
import org.vladg.jafax.ast.repository.indexed.KeyIndexedMethodRepository
import org.vladg.jafax.repository.model.Class
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

    private fun findByKey(key: String) =
            KeyIndexedMethodRepository.findByIndex(key)

    private fun createMethod(binding: IMethodBinding) =
        Method(
            name = binding.name,
            signature = binding.signature(),
            key = binding.key,
            isConstructor = binding.isConstructor,
            isDefaultConstructor = binding.isDefaultConstructor,
            modifiers = binding.modifierSet(),
        )

    private fun setMethodToItsContainer(method: Method) =
            method.container?.addToContainedMethods(method)

    private fun createAndSaveMethodWithRecursiveSafety(binding: IMethodBinding, containerSupplier: () -> Container?): Method =
            createMethod(binding)
                    .also {
                        addMethod(it)
                        it.container = containerSupplier()
                        it.returnType = classUnwrapper.findOrCreateClassForBinding(binding.returnType)
                        it.typeParameters = classUnwrapper.getOrderedClasses(binding.typeParameters)
                        setMethodToItsContainer(it)
                    }

    fun findOrCreateMethodForBinding(binding: IMethodBinding?, useStack: Boolean = false, parentBinding: IBinding? = null): Method? =
            binding?.methodDeclaration?.let {
                findByKey(it.key) ?:
                createAndSaveMethodWithRecursiveSafety(it) {
                    if (!useStack) containerService.getOrCreateContainerForBinding(parentBinding ?: it.getParent())
                    else ContainerStack.popUntilBindingObject(parentBinding ?: it.getParent())
                }
            }

    fun incrementCyclomaticComplexity(node: ASTNode) {
        val container = containerService.findContainer(node)
        if (container is Method) {
            container.incrementComplexity()
        }
    }

    fun typeArgumentsForMethodBinding(binding: IMethodBinding): MutableList<Class?> =
            classUnwrapper.getOrderedClasses(binding.typeArguments)

}
