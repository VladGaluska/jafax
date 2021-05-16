package org.vladg.jafax.utils

import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Class

object GenericTypeService {

    fun getPossibleTypes(clazz: Class?, source: ASTObject): Set<Class> {
        clazz ?: return setOf()
        if (clazz.typeParameters.size != 0) {
            return if (source.isChildOf(clazz.firstContainerClass)) {
                clazz.parameterInstances.filterNotNull().toSet()
            } else {
                val superClass = clazz.firstRealTypeSuperClass
                superClass ?: return setOf()
                return setOf(superClass)
            }
        }
        return setOf(clazz)
    }

}