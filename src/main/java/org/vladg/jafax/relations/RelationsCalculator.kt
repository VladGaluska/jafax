package org.vladg.jafax.relations

import org.vladg.jafax.io.model.Relations
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.MethodFilterService
import org.vladg.jafax.repository.model.Class

object RelationsCalculator {

    fun calculateRelations(): List<Relations> =
            ClassRepository.getTopLevelClasses()
                .groupBy { it.fileName }
                .flatMap { calculateRelations(it.key!!, it.value) }

    private fun calculateRelations(fileName: String, classesInFile: List<Class>): Collection<Relations> {
        val relationsUpdater = RelationsUpdater(fileName)
        calculateExtCalls(classesInFile, relationsUpdater)
        calculateExtData(classesInFile, relationsUpdater)
        calculateHierarchy(classesInFile, relationsUpdater)
        calculateExtDataStrict(classesInFile, relationsUpdater)
        calculateReturns(classesInFile, relationsUpdater)
        calculateAll(classesInFile, relationsUpdater)
        return relationsUpdater.getRelations()
    }

    private fun calculateExtCalls(classesInFile: List<Class>, relationsUpdater: RelationsUpdater) =
            getCalledMethodsByTarget(classesInFile).map { calledMethodsByTarget ->
                relationsUpdater.updateRelationsForTarget(calledMethodsByTarget.key) {
                    it.extCalls = calledMethodsByTarget.value.size
                }
            }

    private fun calculateExtData(classesInFile: List<Class>, relationsUpdater: RelationsUpdater) =
            getAccessedFieldsByTarget(classesInFile).map { accessedFieldsByTarget ->
                relationsUpdater.updateRelationsForTarget(accessedFieldsByTarget.key) {
                    it.extData = accessedFieldsByTarget.value.size
                }
            }

    private fun calculateHierarchy(classesInFile: List<Class>, relationsUpdater: RelationsUpdater) {

    }

    private fun calculateExtDataStrict(classesInFile: List<Class>, relationsUpdater: RelationsUpdater) {

    }

    private fun calculateReturns(classesInFile: List<Class>, relationsUpdater: RelationsUpdater) {

    }

    private fun calculateAll(classesInFile: List<Class>, relationsUpdater: RelationsUpdater) {

    }

    private fun getAccessedFieldsByTarget(classesInFile: List<Class>) =
        getAccessedFields(classesInFile).groupBy { it.fileName!! }

    private fun getAccessedFields(classesInFile: List<Class>) =
        classesInFile.flatMap { it.allFieldAccesses }

    private fun getCalledMethodsByTarget(classesInFile: List<Class>) =
        getCalledMethods(classesInFile).groupBy { it.fileName!! }

    private fun getCalledMethods(classesInFile: List<Class>) =
        MethodFilterService.filterMethods(classesInFile.flatMap { it.allMethodCalls })

}