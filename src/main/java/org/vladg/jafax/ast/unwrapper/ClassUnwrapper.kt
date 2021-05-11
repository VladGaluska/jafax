package org.vladg.jafax.ast.unwrapper

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.ITypeBinding
import org.vladg.jafax.ast.repository.ContainerStack
import org.vladg.jafax.ast.repository.indexed.KeyIndexedClassRepository
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Container
import org.vladg.jafax.utils.extensions.ast.getActualType
import org.vladg.jafax.utils.extensions.ast.getParent
import org.vladg.jafax.utils.extensions.ast.modifierSet
import org.vladg.jafax.utils.extensions.ast.originalType

class ClassUnwrapper {

    @Inject
    private lateinit var containerService: ContainerService

    private fun createClass(binding: ITypeBinding) =
            Class(
                name = binding.name,
                isInterface = binding.isInterface,
                modifiers = binding.modifierSet(),
                key = binding.key,
                isExternal = !binding.isFromSource
            )

    private fun setClassToItsContainer(clazz: Class) =
        clazz.container?.addToContainedClasses(clazz)

    private fun getInterfaces(binding: ITypeBinding): MutableSet<Class> =
        binding.interfaces
            .mapNotNull { findOrCreateClassForBinding(it) }
            .toMutableSet()

    private fun createAndSaveClassWithRecursiveSafety(binding: ITypeBinding, containerSupplier: () -> Container?): Class =
            createClass(binding).apply {
                addClass(this)
                this.container = containerSupplier()
                this.superClass = findOrCreateClassForBinding(binding.superclass)
                this.superInterfaces = getInterfaces(binding)
                setClassToItsContainer(this)
            }

    private fun addClass(clazz: Class) {
        KeyIndexedClassRepository.addObject(clazz)
    }

    private fun findByKey(key: String) =
         KeyIndexedClassRepository.findByIndex(key)

    fun findAll() = KeyIndexedClassRepository.findAll()

    fun findOrCreateClassForBinding(binding: ITypeBinding?, useStack: Boolean = false) =
        binding?.getActualType()?.originalType()?.let {
            findByKey(it.key)
                    ?: createAndSaveClassWithRecursiveSafety(it) {
                        if (!useStack) containerService.getOrCreateContainerForBinding(it.getParent())
                        else ContainerStack.popUntilBindingObject(it.getParent())
                    }
        }

}