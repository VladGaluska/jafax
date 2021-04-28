package org.vladg.jafax.ast.service

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.vladg.jafax.ast.repository.model.Container
import org.vladg.jafax.utils.extensions.ast.bindingNameFromType
import org.vladg.jafax.utils.extensions.ast.immediateContainerBinding
import org.vladg.jafax.utils.extensions.logger

class ContainerService {

    private val logger = logger()

    @Inject
    private lateinit var classService: ClassService

    @Inject
    private lateinit var methodService: MethodService

    fun getOrCreateContainerForBinding(binding: IBinding?, useStack: Boolean = false): Container? {
        binding ?: return null
        return when (binding.kind) {
            IBinding.METHOD -> this.methodService.findOrCreateMethodForBinding(binding as IMethodBinding, useStack)
            IBinding.TYPE -> this.classService.findOrCreateClassForBinding(binding as ITypeBinding, useStack)
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

     fun findContainer(node: ASTNode): Container? {
        val containerBinding = this.getContainerBindingForNode(node)
        return this.getOrCreateContainerForBinding(containerBinding, true)
    }

}