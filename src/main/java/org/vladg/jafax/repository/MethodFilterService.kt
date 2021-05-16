package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.Method

object MethodFilterService {

    private fun getFilter(
            excludeExternal: Boolean,
            excludeAccessors: Boolean,
            excludeImplicitConstructors: Boolean,
            onlyAccessors: Boolean,
            excludeProtected: Boolean,
            onlyProtected: Boolean,
            fileName: String?
    ): (Method) -> Boolean {
        val filters = ArrayList<(Method) -> Boolean>()
        if (excludeExternal) filters.add { it.isInternal && it.fileName != null }
        if (excludeAccessors && !onlyAccessors) filters.add { !it.isAccessor }
        if (excludeImplicitConstructors) filters.add { !it.isDefaultConstructor }
        if (excludeProtected && !onlyProtected) filters.add { !it.isProtected() }
        if (onlyAccessors) filters.add { it.isAccessor }
        if (onlyProtected) filters.add { it.isProtected() }
        if (fileName != null) filters.add { it.fileName != fileName }
        return { method ->
            filters.all { it(method) }
        }
    }

    fun filterMethods(
            methods: List<Method>,
            excludeExternal: Boolean = true,
            excludeAccessors: Boolean = true,
            excludeImplicitConstructors: Boolean = true,
            onlyAccessors: Boolean = false,
            excludeProtected: Boolean = true,
            onlyProtected: Boolean = false,
            fileNameToOmit: String? = null
    ) = methods.filter {
        getFilter(
                excludeExternal,
                excludeAccessors,
                excludeImplicitConstructors,
                onlyAccessors,
                excludeProtected,
                onlyProtected,
                fileName = fileNameToOmit
        )(it)
    }

}