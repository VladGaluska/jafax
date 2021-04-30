package org.vladg.jafax.utils.inject

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import org.vladg.jafax.ast.ASTRequester
import org.vladg.jafax.ast.ASTVisitor
import org.vladg.jafax.ast.service.*
import org.vladg.jafax.io.writer.ProjectLayoutWriter

class DependencyManager : AbstractModule() {

    override fun configure() {
        bind(ASTVisitor::class.java).`in`(Scopes.SINGLETON)
        bind(ClassService::class.java).`in`(Scopes.SINGLETON)
        bind(MethodService::class.java).`in`(Scopes.SINGLETON)
        bind(AttributeService::class.java).`in`(Scopes.SINGLETON)
        bind(ContainerService::class.java).`in`(Scopes.SINGLETON)
        bind(MethodInvocationService::class.java).`in`(Scopes.SINGLETON)
        bind(ASTRequester::class.java).`in`(Scopes.SINGLETON)
        bind(ProjectLayoutWriter::class.java).`in`(Scopes.SINGLETON)
    }

}