package org.vladg.jafax.relations

import org.vladg.jafax.io.model.Relations
import org.vladg.jafax.io.writer.RelationsWriter
import org.vladg.jafax.repository.AttributeFilterService
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.MethodFilterService
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Method
import org.vladg.jafax.utils.GenericTypeService
import org.vladg.jafax.utils.extensions.logger
import java.nio.file.Path

object ExternalRelationsComputer {

    private val primitiveList = listOf("void", "boolean", "int", "float", "double", "char")

    private val logger = logger()

    fun computeRelations(path: Path, projectName: String) {
        logger.info("Beginning relations calculation...")
        RelationsWriter.writeRelationsToFile(
            ClassRepository.topLevelClasses
                .groupBy { it.fileName }
                .flatMap { computeRelations(it.key!!, it.value) }
                .filterNot { it.target.startsWith("java") }
                .filterNot { it.target.startsWith(".") }
                .filterNot { primitiveList.contains(it.target) },
            path,
            projectName
        )
    }

    private fun computeRelations(fileName: String, classesInFile: List<Class>): Collection<Relations> =
        RelationsUpdater(fileName).apply {
            calculateExtCalls(classesInFile, this)
            calculateExtData(classesInFile, this)
            ExternalHierarchyCalculator.calculateHierarchy(classesInFile, this)
            calculateExtDataStrict(classesInFile, this)
            calculateReturns(classesInFile, this)
            calculateDeclarations(classesInFile, this)
        }.getRelations()

    private fun calculateExtCalls(
        classesInFile: List<Class>,
        relationsUpdater: RelationsUpdater
    ) =
        getCalledMethodsByTarget(classesInFile).forEach { calledMethodsByTarget ->
            relationsUpdater.updateRelationsForTarget(calledMethodsByTarget.key) {
                it.extCalls = calledMethodsByTarget.value.size
            }
        }

    private fun calculateExtData(
        classesInFile: List<Class>,
        relationsUpdater: RelationsUpdater
    ) =
        getAccessedFieldsByTarget(classesInFile).forEach { accessedFieldsByTarget ->
            relationsUpdater.updateRelationsForTarget(accessedFieldsByTarget.key) {
                it.extData = accessedFieldsByTarget.value.size
            }
        }

    private fun calculateExtDataStrict(
        classesInFile: List<Class>,
        relationsUpdater: RelationsUpdater
    ) =
        getAccessedFieldsByTarget(classesInFile).forEach { accessedFieldsByTarget ->
            relationsUpdater.updateRelationsForTarget(accessedFieldsByTarget.key) {
                it.extDataStrict = accessedFieldsByTarget.value.size
            }
        }

    private fun calculateReturns(
        classesInFile: List<Class>,
        relationsUpdater: RelationsUpdater
    ) =
        getReturnedTypesByTarget(classesInFile).forEach { returnedTypesByTarget ->
            relationsUpdater.updateRelationsForTarget(returnedTypesByTarget.key) {
                it.returns = returnedTypesByTarget.value.size
            }
        }

    private fun calculateDeclarations(
        classesInFile: List<Class>,
        relationsUpdater: RelationsUpdater
    ) =
        getDeclarationsByTarget(classesInFile).forEach { declarationsByTarget ->
            relationsUpdater.updateRelationsForTarget(declarationsByTarget.key) {
                it.declarations = declarationsByTarget.value.size
            }
        }

    private fun getDeclarationsByTarget(classesInFile: List<Class>) =
        getDeclarations(classesInFile)
            .mapNotNull { it.type }
            .flatMap { GenericTypeService.getPossibleTypes(it, it) }
            .groupBy { it.fullyQualifiedName }

    private fun getDeclarations(classesInFile: List<Class>) =
        AttributeFilterService.filterAttributes(
            classesInFile.flatMap { it.allContainedAttributes },
            excludeProtected = false,
            excludeInternal = true,
            excludeExternal = false,
        )

    private fun getAccessedFieldsByTarget(classesInFile: List<Class>) = getAccessedFields(classesInFile)
        .filterNot { it.topLevelClass == null }
        .groupBy { it.topLevelClass!!.fullyQualifiedName }

    private fun getAccessedFields(classesInFile: List<Class>) =
        AttributeFilterService.filterAttributes(
            classesInFile.flatMap { it.allFieldAccesses },
            excludeInternal = true,
            excludeExternal = false
        )

    private fun getReturnedTypesByTarget(classesInFile: List<Class>) =
        classesInFile.flatMap { it.allReturnTypes }
            .filter { it.isExternal }
            .groupBy { it.fullyQualifiedName }

    private fun getCalledMethodsByTarget(classesInFile: List<Class>) =
        getCalledMethods(classesInFile)
            .filterNot { it.topLevelClass == null }
            .groupBy { it.topLevelClass!!.fullyQualifiedName }

    private fun getCalledMethods(classesInFile: List<Class>) =
        filterMethods(classesInFile.flatMap { it.allMethodCalls })

    private fun filterMethods(methods: List<Method>) =
        MethodFilterService.filterMethods(
            methods,
            excludeInternal = true,
            excludeExternal = false,
        )

}
