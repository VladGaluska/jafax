package org.vladg.jafax.metrics

import org.vladg.jafax.io.model.Metrics
import org.vladg.jafax.io.writer.MetricsWriter
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Method
import org.vladg.jafax.utils.extensions.doubleDiv
import org.vladg.jafax.utils.extensions.logger
import org.vladg.jafax.utils.extensions.roundToTwoDecimals
import java.nio.file.Path

object MetricsComputer {

    private val logger = logger()

    private val changingClasses: MutableMap<Class, MutableSet<Class>> = HashMap()

    private val changingMethods: MutableMap<Class, MutableSet<Method>> = HashMap()

    private val superClassesByExtendingClasses: MutableMap<Class, MutableSet<Class>> = HashMap()

    private val cachedHits: MutableMap<Class, Int> = HashMap()

    fun computeMetrics(path: Path, name: String) {
        logger.info("Beginning metrics calculation...")
        MetricsWriter.writeMetricsToFile(
                computeMetrics(getClassesForMetrics()),
                path,
                name
        )
    }

    private fun getClassesForMetrics() =
            ClassRepository.topLevelClasses

    private fun computeMetrics(classes: List<Class>): List<Metrics> {
        cacheClasses(classes)
        return classes.map { calculateMetricsForClass(it) }
    }

    private fun calculateMetricsForClass(clazz: Class): Metrics =
        Metrics(file = clazz.fileName!!, type = clazz.name).apply {
            AMW = calculateAMW(clazz).roundToTwoDecimals()
            WMC = clazz.totalCyclomaticComplexity
            NOM = clazz.containedMethods.filter { !it.isDefaultConstructor }.size
            NOPA = clazz.containedFields.filter{ attributeQualifiesForNopa(it) }.size
            NProtM = calculateNProtM(clazz)
            computeATFDRelatedMetrics(this, clazz)
            WOC = calculateWOC(clazz).roundToTwoDecimals()
            CC = changingClasses[clazz]?.size ?: 0
            CM = changingMethods[clazz]?.size ?: 0
            computeCINTAndCDISP(this, clazz)
            BOvR = calculateBovr(clazz).roundToTwoDecimals()
            BUR = calculateBur(clazz).roundToTwoDecimals()
            HIT = calculateHit(clazz)
            DIT = clazz.DIT
            NOC = calculateNoc(clazz)
            RFC = calculateRfc(clazz)
        }

    private fun calculateAMW(clazz: Class): Double {
        val amountOfMethods = clazz.containedMethods.filter { !it.isDefaultConstructor }.size
        if (amountOfMethods == 0) return 1.0
        return clazz.totalCyclomaticComplexity doubleDiv amountOfMethods
    }

    private fun calculateRfc(clazz: Class) =
            clazz.containedMethods
                 .filter { !it.isDefaultConstructor }
                 .union(clazz.allContainedMethodCalls.filter { it.topLevelClass != clazz.topLevelClass })
                 .filter { it.isInternal && !it.isAccessor && !it.isDefaultConstructor }
                 .distinct()
                 .size

    private fun calculateNoc(clazz: Class) =
            if (clazz in superClassesByExtendingClasses) superClassesByExtendingClasses[clazz]!!.size
            else 0

    private fun calculateHit(clazz: Class): Int {
        if (clazz in cachedHits) return cachedHits[clazz]!!
        if (clazz in superClassesByExtendingClasses) {
            return 1 + (superClassesByExtendingClasses[clazz]!!.map { calculateHit(it) }.maxOrNull() ?: 0)
        }
        return 0
    }

    private fun calculateBur(clazz: Class): Double {
        val amountOfMembers = membersQualifiedForBur(clazz).size
        if (amountOfMembers == 0) return .0
        clazz.superClass ?: return 1.0
        return amountOfMembers doubleDiv clazz.superClass!!.protectedMembers.size
    }

    private fun membersQualifiedForBur(clazz: Class) =
            clazz.allFieldAccesses
                      .union(clazz.allMethodCalls)
                      .filter { it.isInternal }
                      .filter { it.isProtected() }
                      .filter { clazz.superClass == it.topLevelClass }

    private fun calculateBovr(clazz: Class): Double {
        val amountOfMethods = clazz.containedMethods.filter { !it.isStatic() && !it.isAbstract() }.size
        if (amountOfMethods == 0) return .0
        return clazz.superClassOverridingMethods.filter { !it.isStatic() && !it.isAbstract() }.size doubleDiv amountOfMethods
    }

    private fun computeCINTAndCDISP(metrics: Metrics, clazz: Class) {
        val methods = methodsForCINT(clazz)
        metrics.CINT = methods.size
        metrics.CDISP = (if (metrics.CINT != 0) methods.map { it.topLevelClass }.distinct().size doubleDiv metrics.CINT
                        else .0).roundToTwoDecimals()
    }

    private fun methodsForCINT(clazz: Class) =
        clazz.allContainedMethodCalls
             .filter { it.isInternal }
             .filter { !it.isDefaultConstructor }
             .filter { it.topLevelClass != null && it.topLevelClass != clazz }
             .distinct()

    private fun calculateWOC(clazz: Class): Double {
        val amountOfPublicMembers = clazz.allPublicMembers.filter { !it.isAbstract() }.size
        if (amountOfPublicMembers == 0) return 1.0
        return clazz.functionalMethods.size doubleDiv amountOfPublicMembers
    }

    private fun computeATFDRelatedMetrics(metrics: Metrics, clazz: Class) {
        val atfdAttributes = computeATFDAttributes(clazz)
        metrics.ATFD = atfdAttributes.size
        metrics.FDP = calculateFDP(atfdAttributes)
        metrics.ATFD2 = atfdAttributes.filter { it.type?.isInternal == true }.size
    }

    private fun calculateFDP(attributes: Collection<Attribute>) =
            attributes.mapNotNull { it.topLevelClass }.distinct().size

    private fun computeATFDAttributes(clazz: Class) =
            clazz.attributesForATFD
                 .filter { attributeQualifiesForATFD(clazz, it) }

    private fun attributeQualifiesForATFD(
            clazz: Class,
            accessedAttribute: Attribute
    ) = accessedAttribute.container?.isInternal == true &&
        accessedAttribute.firstContainerClass?.isRelatedTo(clazz) == false &&
        accessedAttribute !in clazz.allContainedAttributes

    private fun calculateNProtM(clazz: Class) =
            clazz.protectedMembers.filter { if (it is Method) !it.isConstructor else true }.size

    private fun attributeQualifiesForNopa(attribute: Attribute) =
            attribute.isPublic() && !attribute.isStatic() && !attribute.isFinal()

    private fun cacheClasses(topLevelClasses: List<Class>) {
        topLevelClasses.onEach { clazz ->
            cacheClassForCC(clazz)
            cacheClassForCM(clazz)
            cacheSuperClassesByExtendingClasses(clazz)
        }
    }

    private fun cacheSuperClassesByExtendingClasses(clazz: Class) {
        if (clazz.superClass != null) {
            superClassesByExtendingClasses.computeIfAbsent(clazz.superClass!!) {
                HashSet()
            }.add(clazz)
        }
    }

    private fun cacheClassForCM(clazz: Class) =
            clazz.containedMethods
                 .union(clazz.containedClasses.flatMap { it.containedMethods })
                 .onEach { method ->
                     method.allMethodCalls
                           .filter { !it.isDefaultConstructor && it.isInternal }
                           .filter { it.topLevelClass != null && it.topLevelClass != clazz}
                           .onEach { changingMethods.computeIfAbsent(it.topLevelClass!!) {
                                   HashSet()
                               }.add(method)
                           }
                 }

    private fun cacheClassForCC(clazz: Class) =
            clazz.allContainedMethodCalls
                 .asSequence()
                 .filter { !it.isDefaultConstructor && it.isInternal }
                 .mapNotNull { it.topLevelClass }
                 .filter { it != clazz }
                 .onEach { changingClasses.computeIfAbsent(it){
                         HashSet()
                    }.add(clazz)
                 }
                 .toList()

}