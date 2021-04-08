package org.vladg.ast;

import com.google.inject.Guice;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.vladg.jafax.utils.inject.DependencyManager;

@Slf4j
public class ASTRequester extends FileASTRequestor {

    private final ASTVisitor astVisitor;

    public ASTRequester() {
        var injector = Guice.createInjector(new DependencyManager());
        this.astVisitor = injector.getInstance(ASTVisitor.class);
    }

    @Override
    public void acceptAST(String sourceFilePath, CompilationUnit ast) {
        log.info("Scanning file: " + sourceFilePath);
        astVisitor.setCurrentFileName(sourceFilePath);
        ast.accept(astVisitor);
    }

}
