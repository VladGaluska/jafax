package org.vladg.jafax.utils.inject

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import com.google.inject.TypeLiteral
import org.vladg.ast.ASTVisitor
import org.vladg.ast.repository.NonPersistentRepository
import org.vladg.ast.repository.model.Class
import org.vladg.ast.repository.model.File
import org.vladg.ast.repository.model.Method
import org.vladg.ast.service.FileService

class DependencyManager : AbstractModule() {

    override fun configure() {
        bind(ASTVisitor::class.java).`in`(Scopes.SINGLETON)
        bind(FileService::class.java).`in`(Scopes.SINGLETON)
        bind(typeLiteral<NonPersistentRepository<File>>()).`in`(Scopes.SINGLETON)
        bind(typeLiteral<NonPersistentRepository<Method>>()).`in`(Scopes.SINGLETON)
        bind(typeLiteral<NonPersistentRepository<Class>>()).`in`(Scopes.SINGLETON)
    }

    private inline fun <reified T> typeLiteral() = object : TypeLiteral<T>() {}

}