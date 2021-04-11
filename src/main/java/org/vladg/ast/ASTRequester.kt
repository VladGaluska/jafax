package org.vladg.ast

import com.google.inject.Guice
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.FileASTRequestor
import org.vladg.jafax.utils.inject.DependencyManager
import org.vladg.logger

class ASTRequester : FileASTRequestor() {

    private val logger = logger()

    private val astVisitor: ASTVisitor

    init {
        val injector = Guice.createInjector(DependencyManager())
        astVisitor = injector.getInstance(ASTVisitor::class.java)
    }


    override fun acceptAST(sourceFilePath: String, ast: CompilationUnit) {
        logger.info("Scanning file: $sourceFilePath")
        ast.accept(astVisitor)
    }

}