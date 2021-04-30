package org.vladg.jafax.ast.service

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.ITypeBinding
import org.vladg.jafax.ast.repository.indexed.KeyIndexedClassRepository
import org.vladg.jafax.ast.repository.NonPersistentRepository
import org.vladg.jafax.ast.repository.model.Class
import org.vladg.jafax.ast.repository.model.Container
import org.vladg.jafax.utils.extensions.ast.getActualType
import org.vladg.jafax.utils.extensions.ast.getParent
import org.vladg.jafax.utils.extensions.ast.modifierSet

class ClassService {

    @Inject
    private lateinit var containerService: ContainerService

    private fun createClass(binding: ITypeBinding, containerSupplier: () -> Container?): Class {
        val clazz = Class(
            name = binding.name,
            isInterface = binding.isInterface,
            modifiers = binding.modifierSet(),
            key = binding.key,
            superClass = findOrCreateClassForBinding(binding.superclass),
            superInterfaces = getInterfaces(binding),
            isExternal = !binding.isFromSource
        )
        containerSupplier()?.addToContainedClasses(clazz)
        return clazz
    }

    private fun getInterfaces(binding: ITypeBinding): MutableSet<Class> =
        binding.interfaces
            .mapNotNull { findOrCreateClassForBinding(it) }
            .toMutableSet()

    private fun createAndSaveClass(binding: ITypeBinding, containerSupplier: () -> Container?): Class {
        val clazz = createClass(binding, containerSupplier)
        addClass(clazz)
        return clazz
    }

/*
    fun salvageFromDeclaration(typeDeclaration: TypeDeclaration): Class {
        return Class(
            name = typeDeclaration.name.fullyQualifiedName,
            isInterface = typeDeclaration.isInterface,
            isSource = false,
            modifiers = typeDeclaration.modifierList(),
        )
    }
*/

    private fun addClass(clazz: Class) {
        KeyIndexedClassRepository.addObject(clazz)
    }

    fun findByKey(key: String): Class? {
        return KeyIndexedClassRepository.findByKey(key)
    }

    fun findAll(): MutableCollection<Class> {
        return KeyIndexedClassRepository.findAll()
    }

    fun findOrCreateClassForBinding(binding: ITypeBinding?, useStack: Boolean = false): Class? {
        val arrayCheckedBinding = binding?.getActualType() ?: return null
        return findByKey(arrayCheckedBinding.key)
            ?: createAndSaveClass(arrayCheckedBinding) {
                if (!useStack) containerService.getOrCreateContainerForBinding(arrayCheckedBinding.getParent())
                else NonPersistentRepository.popUntilBindingObject(arrayCheckedBinding.getParent())
            }
    }

}