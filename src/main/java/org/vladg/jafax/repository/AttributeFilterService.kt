package org.vladg.jafax.repository

import org.vladg.jafax.repository.model.Attribute

object AttributeFilterService {

    private fun getFilter(
            excludeExternal: Boolean,
            excludeProtected: Boolean,
            excludeExternalType: Boolean,
            onlyProtected: Boolean,
            omittedFileName: String?
    ): (Attribute) -> Boolean {
        val filters = ArrayList<(Attribute) -> Boolean>()
        if (excludeExternal) filters.add { it.isInternal && it.fileName != null }
        if (excludeProtected && !onlyProtected) filters.add { !it.isProtected() }
        if (onlyProtected) filters.add { it.isProtected() }
        if (omittedFileName != null) filters.add { it.fileName != omittedFileName }
        if (excludeExternalType) filters.add { it.type?.isInternal == true }
        return { attribute ->
            filters.all { it(attribute) }
        }
    }

    fun filterAttributes(
            attributes: List<Attribute>,
            excludeExternal: Boolean = true,
            excludeProtected: Boolean = true,
            excludeExternalType: Boolean = false,
            onlyProtected: Boolean = false,
            omittedFileName: String? = null
    ) = attributes.filter {
        getFilter(excludeExternal, excludeProtected, excludeExternalType, onlyProtected, omittedFileName)(it)
    }

}