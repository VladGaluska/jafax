package org.vladg.jafax.utils

import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Class

object GenericTypeService {

    private fun getParameterInstances(clazz: Class): Set<Class> {
        if (clazz.isTypeParameter) {
            return clazz.parameterInstances
                    .union(listOf(clazz.superClass))
                    .filterNotNull()
                    .flatMap { getParameterInstances(it) }
                    .toSet()
        }
        return setOf(clazz)
    }

    fun getPossibleTypes(clazz: Class?, source: ASTObject): Set<Class> {
        clazz ?: return setOf()
        if (clazz.isTypeParameter) {
            return if (source.topLevelClass == clazz.topLevelClass) {
                getParameterInstances(clazz)
            } else {
                val superClass = clazz.firstRealTypeSuperClass
                superClass ?: return setOf()
                return setOf(superClass)
            }
        }
        return setOf(clazz)
    }

}