package org.vladg.ast

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.eclipse.jdt.core.dom.ASTVisitor
import org.vladg.ast.repository.model.File
import org.vladg.ast.service.FileService
import kotlin.streams.toList

class ASTVisitor : ASTVisitor() {

    @Inject
    private lateinit var fileService: FileService

    override fun visit(typeDeclaration: TypeDeclaration): Boolean {
        return true
    }

    override fun visit(packageDeclaration: PackageDeclaration): Boolean {
        return true
    }

    override fun visit(methodDeclaration: MethodDeclaration): Boolean {
        return true
    }

    override fun visit(fieldDeclaration: FieldDeclaration): Boolean {
        return true
    }

    override fun visit(singleVariableDeclaration: SingleVariableDeclaration): Boolean {
        return true
    }

    override fun visit(node: MethodInvocation): Boolean {
        return true
    }

    override fun visit(node: CompilationUnit): Boolean {
        if (node.module != null) {
            return true
        }
        val imports = (node.imports() as List<ImportDeclaration>)
            .stream()
            .map { it.name.fullyQualifiedName }
            .toList()
        val astPackage: PackageDeclaration? = node.getPackage()
        val packageName = astPackage?.name?.fullyQualifiedName
        fileService.addFile(File(packageName = packageName, imports = imports, name = ""))
        return true
    }
}