package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.Method

object MethodFilterService {

    private val filtersForMethods = mapOf<String, (Method) -> Boolean>(
            "excludeExternal" to { it.isInternal() },
            "excludeAccessors" to { !it.isAccessor() },
            "excludeImplicitConstructors" to { !it.isDefaultConstructor },
            "excludeProtected" to { !it.isProtected() }
    )

    private fun getFilter(
            excludeExternal: Boolean,
            excludeAccessors: Boolean,
            excludeImplicitConstructors: Boolean,
            excludeProtected: Boolean
    ): (Method) -> Boolean {
        val filters = ArrayList<(Method) -> Boolean>()
        if (excludeExternal) filters.add(filtersForMethods["excludeExternal"]!!)
        if (excludeAccessors) filters.add(filtersForMethods["excludeAccessors"]!!)
        if (excludeImplicitConstructors) filters.add(filtersForMethods["excludeImplicitConstructors"]!!)
        if (excludeProtected) filters.add(filtersForMethods["excludeProtected"]!!)
        return { method ->
            filters.all { it(method) }
        }
    }

    fun filterMethods(
            methods: List<Method>,
            excludeExternal: Boolean = true,
            excludeAccessors: Boolean = true,
            excludeImplicitConstructors: Boolean = true,
            excludeProtected: Boolean = true
    ) = methods.filter {
        getFilter(excludeExternal, excludeAccessors, excludeImplicitConstructors, excludeProtected)(it)
    }

}