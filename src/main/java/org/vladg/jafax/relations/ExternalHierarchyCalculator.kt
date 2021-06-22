package org.vladg.jafax.relations

import org.vladg.jafax.repository.AttributeFilterService
import org.vladg.jafax.repository.MethodFilterService
import org.vladg.jafax.repository.model.Class

object ExternalHierarchyCalculator {

    fun calculateHierarchy(classes: List<Class>, relationsUpdater: RelationsUpdater) {
        updateHierarchy(relationsUpdater) { getBasicHierarchyRelations(classes) }
        updateHierarchy(relationsUpdater) { getOverrideRelations(classes) }
    }

    private fun updateHierarchy(relationsUpdater: RelationsUpdater, hierarchyProvider: () -> Map<String, List<*>>) =
        hierarchyProvider().forEach {
            updateWithHierarchy(it.key, it.value.count(), relationsUpdater)
        }

    private fun updateWithHierarchy(targetName: String, valueToUpdateWith: Int, relationsUpdater: RelationsUpdater) {
        relationsUpdater.updateRelationsForTarget(targetName) {
            it.hierarchy = it.hierarchy + valueToUpdateWith
        }
    }

    private fun getOverrideRelations(classes: List<Class>) =
        classes.flatMap { it.overridingMethodsByClass.asSequence() }
            .filter { it.key.isExternal }
            .groupBy({ it.key.fullyQualifiedName }, { it.value })
            .mapValues { it.value.flatten() }

    private fun getBasicHierarchyRelations(classes: List<Class>) =
        getMethodsForHierarchy(classes)
            .union(getAttributesForHierarchy(classes))
            .union(getSuperClasses(classes))
            .filterNot { it.isInternal }
            .filterNot { it.topLevelClass == null }
            .groupBy { it.topLevelClass!!.fullyQualifiedName }

    private fun getSuperClasses(classes: List<Class>) =
        classes.flatMap { it.superInterfaces }
            .union(classes.map { it.superClass })
            .filterNotNull()
            .filterNot { it.isInternal }

    private fun getMethodsForHierarchy(classes: List<Class>) =
        MethodFilterService.filterMethods(
            classes.flatMap { it.allMethodCalls },
            onlyProtected = true,
            excludeInternal = true,
            excludeExternal = false
        )

    private fun getAttributesForHierarchy(classes: List<Class>) =
        AttributeFilterService.filterAttributes(
            classes.flatMap { it.allFieldAccesses },
            onlyProtected = true,
            excludeInternal = true,
            excludeExternal = false
        )

}
