package org.vladg.jafax.relations

import org.vladg.jafax.repository.AttributeFilterService
import org.vladg.jafax.repository.MethodFilterService
import org.vladg.jafax.repository.model.Class

object HierarchyCalculator {

    fun calculateHierarchy(omittedFileName: String, classes: List<Class>, relationsUpdater: RelationsUpdater) {
        updateHierarchy(relationsUpdater) { getBasicHierarchyRelations(omittedFileName, classes) }
        updateHierarchy(relationsUpdater) { getOverrideRelations(omittedFileName, classes) }
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

    private fun getOverrideRelations(omittedFileName: String, classes: List<Class>) =
        classes.flatMap { it.overridingMethodsByClass.asSequence() }
               .filter { it.key.fileName != null }
               .filter { it.key.fileName != omittedFileName }
               .groupBy({ it.key.fileName!! }, { it.value })
               .mapValues { it.value.flatten() }

    private fun getBasicHierarchyRelations(omittedFileName: String, classes: List<Class>) =
        getMethodsForHierarchy(omittedFileName, classes)
                .union(getAttributesForHierarchy(omittedFileName, classes))
                .union(getSuperClasses(omittedFileName, classes))
                .filter { it.fileName != null }
                .groupBy { it.fileName!! }

    private fun getSuperClasses(omittedFileName: String, classes: List<Class>) =
            classes.flatMap { it.superInterfaces }
                   .union(classes.map { it.superClass })
                   .filterNotNull()
                   .filter { it.isInternal }
                   .filter { it.fileName != omittedFileName }

    private fun getMethodsForHierarchy(omittedFileName: String, classes: List<Class>) =
        MethodFilterService.filterMethods(
                classes.flatMap { it.calledMethods },
                onlyProtected = true,
                fileNameToOmit = omittedFileName
        )

    private fun getAttributesForHierarchy(omittedFileName: String, classes: List<Class>) =
        AttributeFilterService.filterAttributes(
                classes.flatMap { it.accessedFields },
                onlyProtected = true,
                omittedFileName = omittedFileName
        )

}