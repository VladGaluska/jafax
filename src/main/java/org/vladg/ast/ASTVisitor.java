package org.vladg.ast;

import org.vladg.ast.repository.model.File;
import org.vladg.ast.service.FileService;
import com.google.inject.Inject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.core.dom.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ASTVisitor extends org.eclipse.jdt.core.dom.ASTVisitor {

    @Setter
    private String currentFileName;

    @Inject
    private FileService fileService;

    @Override
    public boolean visit(TypeDeclaration typeDeclaration) {
        return true;
    }

    public boolean visit(PackageDeclaration packageDeclaration) {
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration methodDeclaration) {
        return true;
    }

    @Override
    public boolean visit(FieldDeclaration fieldDeclaration) {
        return true;
    }

    @Override
    public boolean visit(SingleVariableDeclaration singleVariableDeclaration) {
        return true;
    }

    @Override
    public boolean visit(MethodInvocation node) {
        return true;
    }

    @Override
    public boolean visit(CompilationUnit node) {
        if (node.getModule() != null) {
            return true;
        }
        var imports = ((List<ImportDeclaration>)node.imports())
                                                               .stream()
                                                               .map(it -> it.getName().getFullyQualifiedName())
                                                               .collect(Collectors.toList());
        var astPackage = node.getPackage();
        var packageName = "";
        if (astPackage != null) {
            packageName = astPackage.getName().getFullyQualifiedName();
        }
        this.fileService.addFile(File.builder()
                                     .name(this.currentFileName)
                                     .packageName(packageName)
                                     .imports(imports)
                                     .build());
        return true;
    }

}
