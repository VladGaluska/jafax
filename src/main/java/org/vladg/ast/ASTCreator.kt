package org.vladg.ast

import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.vladg.logger
import org.vladg.stream
import kotlin.streams.toList

object ASTCreator {

    private val logger = logger()

    fun createAst(javaFiles: Array<String>, jarFiles: Array<String>) {
        logger.info("Creating the AST Request...")
        val parser = ASTParser.newParser(AST.JLS14)
        parser.setResolveBindings(true)
        parser.setKind(ASTParser.K_COMPILATION_UNIT)
        setParserOptions(parser)
        parser.setEnvironment(jarFiles, emptyArray(), emptyArray(), true)
        val encodings = javaFiles.stream()
            .map { "UTF-8" }
            .toList()
            .toTypedArray()
        parser.createASTs(javaFiles, encodings, emptyArray(), ASTRequester(), null)
    }

    private fun setParserOptions(parser: ASTParser) {
        val options = JavaCore.getOptions()
        options[JavaCore.COMPILER_COMPLIANCE] = JavaCore.VERSION_1_8
        options[JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM] = JavaCore.VERSION_1_8
        options[JavaCore.COMPILER_SOURCE] = JavaCore.VERSION_1_8
        parser.setCompilerOptions(options)
    }

}