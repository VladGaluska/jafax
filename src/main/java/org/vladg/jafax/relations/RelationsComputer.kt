package org.vladg.jafax.relations

import org.vladg.jafax.io.model.Relations
import org.vladg.jafax.io.writer.RelationsWriter
import org.vladg.jafax.repository.AttributeFilterService
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.MethodFilterService
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Method
import org.vladg.jafax.utils.extensions.logger
import java.nio.file.Path

object RelationsComputer {

    private val logger = logger()

    fun computeRelations(path: Path) {
        logger.info("Beginning relations calculation...")
        RelationsWriter.writeRelationsToFile(
            ClassRepository.getTopLevelClasses()
                           .groupBy { it.fileName }
                           .flatMap { computeRelations(it.key!!, it.value) },
            path
        )
    }

    private fun computeRelations(fileName: String, classesInFile: List<Class>): Collection<Relations> {
        val relationsUpdater = RelationsUpdater(fileName)
        calculateExtCalls(fileName, classesInFile, relationsUpdater)
        calculateExtData(fileName, classesInFile, relationsUpdater)
        HierarchyCalculator.calculateHierarchy(fileName, classesInFile, relationsUpdater)
        calculateExtDataStrict(fileName, classesInFile, relationsUpdater)
        calculateReturns(fileName, classesInFile, relationsUpdater)
        calculateDeclarations(fileName, classesInFile, relationsUpdater)
        return relationsUpdater.getRelations()
    }

    private fun calculateExtCalls(omittedFileName: String, classesInFile: List<Class>, relationsUpdater: RelationsUpdater) =
            getCalledMethodsByTarget(omittedFileName, classesInFile).forEach { calledMethodsByTarget ->
                relationsUpdater.updateRelationsForTarget(calledMethodsByTarget.key) {
                    it.extCalls = calledMethodsByTarget.value.size
                }
            }

    private fun calculateExtData(omittedFileName: String, classesInFile: List<Class>, relationsUpdater: RelationsUpdater) =
            getAccessedFieldsByTarget(omittedFileName, false, classesInFile).forEach { accessedFieldsByTarget ->
                relationsUpdater.updateRelationsForTarget(accessedFieldsByTarget.key) {
                    it.extData = accessedFieldsByTarget.value.size
                }
            }

    private fun calculateExtDataStrict(omittedFileName: String, classesInFile: List<Class>, relationsUpdater: RelationsUpdater) =
            getAccessedFieldsByTarget(omittedFileName, true, classesInFile).forEach { accessedFieldsByTarget ->
                relationsUpdater.updateRelationsForTarget(accessedFieldsByTarget.key) {
                    it.extDataStrict = accessedFieldsByTarget.value.size
                }
            }

    private fun calculateReturns(omittedFileName: String, classesInFile: List<Class>, relationsUpdater: RelationsUpdater) =
            getReturnedTypesByTarget(omittedFileName, classesInFile).forEach { returnedTypesByTarget ->
                relationsUpdater.updateRelationsForTarget(returnedTypesByTarget.key) {
                    it.returns = returnedTypesByTarget.value.size
                }
            }

    private fun calculateDeclarations(omittedFileName: String, classesInFile: List<Class>, relationsUpdater: RelationsUpdater) =
            getDeclarationsByTarget(omittedFileName, classesInFile).forEach { declarationsByTarget ->
                relationsUpdater.updateRelationsForTarget(declarationsByTarget.key) {
                    it.declarations = declarationsByTarget.value.size
                }
            }

    private fun getDeclarationsByTarget(omittedFileName: String, classesInFile: List<Class>) =
        getDeclarations(omittedFileName, classesInFile)
                .mapNotNull { it.type }
                .flatMap {
                    if (it.isTypeParameter) it.parameterInstances
                    else listOf(it)
                }
                .filterNotNull()
                .groupBy { it.fileName!! }

    private fun getDeclarations(omittedFileName: String, classesInFile: List<Class>) =
        AttributeFilterService.filterAttributes(
                classesInFile.flatMap { it.allContainedAttributes },
                omittedFileName = omittedFileName
        )

    private fun getAccessedFieldsByTarget(omittedFileName: String, excludeProtected: Boolean, classesInFile: List<Class>) =
        getAccessedFields(omittedFileName, excludeProtected, classesInFile).groupBy { it.fileName!! }

    private fun getAccessedFields(omittedFileName: String, excludeProtected: Boolean, classesInFile: List<Class>) =
        AttributeFilterService.filterAttributes(
                classesInFile.flatMap { it.allFieldAccesses },
                excludeProtected = excludeProtected,
                omittedFileName = omittedFileName
        )

    private fun getReturnedTypesByTarget(omittedFileName: String, classesInFile: List<Class>) =
            classesInFile.flatMap { it.allReturnTypes }
                         .filter { it.fileName != null && it.fileName != omittedFileName }
                         .groupBy { it.fileName!! }

    private fun getCalledMethodsByTarget(omittedFileName: String, classesInFile: List<Class>) =
        getCalledMethods(omittedFileName, classesInFile).groupBy { it.fileName!! }

    private fun getCalledMethods(omittedFileName: String, classesInFile: List<Class>) =
        filterMethods(omittedFileName, classesInFile.flatMap { it.allMethodCalls })

    private fun filterMethods(omittedFileName: String? = null, methods: List<Method>) =
        MethodFilterService.filterMethods(methods, fileNameToOmit = omittedFileName)

}