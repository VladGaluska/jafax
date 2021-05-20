package org.vladg.jafax.ast

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.FileASTRequestor
import org.vladg.jafax.utils.extensions.convertFilePathToUniversalPath
import org.vladg.jafax.utils.extensions.logger

class ASTRequester : FileASTRequestor() {

    private val logger = logger()

    @Inject
    private lateinit var astVisitor: ASTVisitor

    override fun acceptAST(sourceFilePath: String, ast: CompilationUnit) {
        val universalPath = convertFilePathToUniversalPath(sourceFilePath)
        logger.info("Scanning file: $universalPath")
        astVisitor.currentFileName = universalPath
        try {
            ast.accept(astVisitor)
        } catch (ex: Exception) {
            logger.error("There was an error while ast parsing, will skip that entry ...", ex)
        }
    }

}