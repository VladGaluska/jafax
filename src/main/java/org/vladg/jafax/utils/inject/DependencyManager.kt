package org.vladg.jafax.utils.inject

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import org.vladg.jafax.ast.ASTRequester
import org.vladg.jafax.ast.ASTVisitor
import org.vladg.jafax.ast.repository.AttributeRepository
import org.vladg.jafax.ast.repository.ClassRepository
import org.vladg.jafax.ast.repository.CommonRepository
import org.vladg.jafax.ast.repository.MethodRepository
import org.vladg.jafax.ast.service.*
import org.vladg.jafax.io.writer.ProjectLayoutWriter

class DependencyManager : AbstractModule() {

    override fun configure() {
        bind(ASTVisitor::class.java).`in`(Scopes.SINGLETON)
        bind(ClassRepository::class.java).`in`(Scopes.SINGLETON)
        bind(MethodRepository::class.java).`in`(Scopes.SINGLETON)
        bind(AttributeRepository::class.java).`in`(Scopes.SINGLETON)
        bind(CommonRepository::class.java).`in`(Scopes.SINGLETON)
        bind(ClassService::class.java).`in`(Scopes.SINGLETON)
        bind(MethodService::class.java).`in`(Scopes.SINGLETON)
        bind(AttributeService::class.java).`in`(Scopes.SINGLETON)
        bind(ContainerService::class.java).`in`(Scopes.SINGLETON)
        bind(MethodInvocationService::class.java).`in`(Scopes.SINGLETON)
        bind(ASTRequester::class.java).`in`(Scopes.SINGLETON)
        bind(ProjectLayoutWriter::class.java).`in`(Scopes.SINGLETON)
    }

}