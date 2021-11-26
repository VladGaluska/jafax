package org.vladg.jafax.ast.unwrapper

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.vladg.jafax.ast.unwrapper.clazz.ClassUnwrapper
import org.vladg.jafax.ast.unwrapper.method.MethodUnwrapper
import org.vladg.jafax.repository.model.container.Class
import org.vladg.jafax.repository.model.container.Container
import org.vladg.jafax.utils.extensions.ast.bindingNameFromType
import org.vladg.jafax.utils.extensions.ast.immediateContainerBinding
import org.vladg.jafax.utils.extensions.logger

class ContainerService {

    private val logger = logger()

    @Inject
    private lateinit var classUnwrapper: ClassUnwrapper

    @Inject
    private lateinit var methodUnwrapper: MethodUnwrapper

    fun getOrCreateContainerForBinding(binding: IBinding?, useStack: Boolean = false): Container? {
        binding ?: return null
        return when (binding.kind) {
            IBinding.METHOD -> methodUnwrapper.findOrCreateMethodForBinding(binding as IMethodBinding, useStack)
            IBinding.TYPE -> classUnwrapper.findOrCreateClassForBinding(binding as ITypeBinding, useStack)
            else -> throw IllegalStateException("Containers of type: " + bindingNameFromType(binding.kind) + " not supported!")
        }
    }

    private fun getContainerBindingForNode(node: ASTNode): IBinding? {
        val (binding, shouldExist) = node.immediateContainerBinding()
        if (shouldExist) {
            if (binding == null) {
                logger.warn("Could not resolve binding for a node of type: ${node.nodeType}")
            }
        }
        return binding
    }

     fun findContainer(node: ASTNode, useStack: Boolean = true): Container? {
        val containerBinding = getContainerBindingForNode(node)
        return getOrCreateContainerForBinding(containerBinding, useStack)
     }

     private fun addToParameterInstance(parameterizedContainer: Container?, index: Int, classToAdd: Class?) {
         parameterizedContainer?.typeParameters?.get(index)?.addToParameterInstances(classToAdd)
     }

    fun setParameterInstances(parameterizedContainer: Container?, typeInstances: List<Class?>) {
        typeInstances.forEachIndexed { index, clazz ->
            addToParameterInstance(parameterizedContainer, index, clazz)
        }
    }


}