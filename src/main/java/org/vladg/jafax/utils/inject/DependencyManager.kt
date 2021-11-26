package org.vladg.jafax.utils.inject

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import org.vladg.jafax.ast.ASTRequester
import org.vladg.jafax.ast.ASTVisitor
import org.vladg.jafax.ast.unwrapper.*
import org.vladg.jafax.ast.unwrapper.attribute.AttributeUnwrapper
import org.vladg.jafax.ast.unwrapper.clazz.ClassUnwrapper
import org.vladg.jafax.ast.unwrapper.method.MethodInvocationUnwrapper
import org.vladg.jafax.ast.unwrapper.method.MethodUnwrapper
import org.vladg.jafax.io.writer.ProjectLayoutWriter

class DependencyManager : AbstractModule() {

    override fun configure() {
        bind(ASTVisitor::class.java).`in`(Scopes.SINGLETON)
        bind(ClassUnwrapper::class.java).`in`(Scopes.SINGLETON)
        bind(MethodUnwrapper::class.java).`in`(Scopes.SINGLETON)
        bind(AttributeUnwrapper::class.java).`in`(Scopes.SINGLETON)
        bind(ContainerService::class.java).`in`(Scopes.SINGLETON)
        bind(MethodInvocationUnwrapper::class.java).`in`(Scopes.SINGLETON)
        bind(ASTRequester::class.java).`in`(Scopes.SINGLETON)
        bind(ProjectLayoutWriter::class.java).`in`(Scopes.SINGLETON)
    }

}