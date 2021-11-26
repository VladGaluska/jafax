package org.vladg.jafax.utils

import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.container.Class

object GenericTypeService {

    private var callChain = HashSet<Class>()

    private fun getParameterInstances(clazz: Class): Set<Class> {
        if (clazz in callChain) {
            return salvageSuperClassFromParameterized(clazz)
        }
        callChain.add(clazz)
        return getInstances(clazz)
    }

    private fun getInstances(clazz: Class) =
            if (clazz.isTypeParameter)
                clazz.parameterInstances
                     .union(listOf(clazz.superClass))
                     .filterNotNull()
                     .flatMap { getParameterInstances(it) }
                     .toSet()
            else setOf(clazz)

    fun getPossibleTypes(clazz: Class?, source: ASTObject): Set<Class> {
        clazz ?: return setOf()
        callChain = HashSet()
        if (clazz.isTypeParameter) {
            return if (source.topLevelClass == clazz.topLevelClass) getParameterInstances(clazz)
                   else salvageSuperClassFromParameterized(clazz)
        }
        return setOf(clazz)
    }

    private fun salvageSuperClassFromParameterized(clazz: Class): Set<Class> {
        val superClass = clazz.firstRealTypeSuperClass
        superClass ?: return setOf()
        return setOf(superClass)
    }

}