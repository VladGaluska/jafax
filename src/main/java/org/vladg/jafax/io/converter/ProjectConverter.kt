package org.vladg.jafax.io.converter

import org.vladg.jafax.io.model.tree.Method as TreeMethod
import org.vladg.jafax.io.model.tree.Package
import org.vladg.jafax.io.model.tree.Project
import org.vladg.jafax.io.model.tree.TreeClass
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Method

object ProjectConverter {

    private val callersByMethodCalled: MutableMap<Long, MutableSet<Long>> = HashMap()

    private val dependenciesByClass: MutableMap<Long, Set<String>> = HashMap()

    fun convertToTree(path: String) =
        Project(
            name = path,
            packages = getClassesByPackageAndCacheDependenciesAndCallers().map { convertToPackage(it.key, it.value) }.toSet()
        )

    private fun convertToPackage(packageName: String, classes: List<Class>) =
        Package(
            id = ASTObject.getAndIncrement(),
            name = packageName,
            classes = classes.map(this::convertToTreeClass).toSet()
        )

    private fun convertToTreeClass(clazz: Class) =
        TreeClass(
            id = clazz.id,
            name = clazz.name,
            methods = clazz.containedMethods.filter{ !it.isAccessor && !it.isConstructor }.map(this::convertToMethod).toSet(),
            dependencies = dependenciesByClass[clazz.id] ?: HashSet()
        )

    private fun cacheDependenciesAndCallers(clazz: Class) {
        dependenciesByClass[clazz.id] =
            clazz.containedMethods.filter { !it.isAccessor && !it.isConstructor }
                .map { Pair(it.id, it.calledMethods.filter { called -> !called.isAccessor && !called.isConstructor }) }
                .onEach {
                    it.second.forEach { called -> callersByMethodCalled.computeIfAbsent(called.id) { HashSet() }.add(it.first) }
                }
                .flatMap { it.second }
                .asSequence()
                .mapNotNull { it.topLevelClass }
                .filter { it.isExternal }
                .distinct()
                .map { "${it.pack}.${it.name}" }
                .toSet()
    }

    private fun convertToMethod(method: Method) =
        TreeMethod(
            id = method.id,
            signature = method.signature,
            callers = callersByMethodCalled[method.id] ?: HashSet(),
            calledMethods = method.calledMethods.filter { !it.isAccessor && !it.isConstructor && it.isInternal }.map { it.id }.toSet()
        )

    private fun getClassesByPackageAndCacheDependenciesAndCallers() =
        ClassRepository.topLevelClasses.filter { !it.isExternal && !it.isInterface && it.pack.isNotBlank() }
            .onEach(this::cacheDependenciesAndCallers)
            .groupBy { it.pack }

}