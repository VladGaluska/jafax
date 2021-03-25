package ast;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import utils.inject.DependencyManager;

public class ASTRequester extends FileASTRequestor {

    @Inject
    private final ASTVisitor astVisitor;

    public ASTRequester() {
        Injector injector = Guice.createInjector(new DependencyManager());
        this.astVisitor = injector.getInstance(ASTVisitor.class);
    }

    @Override
    public void acceptAST(String sourceFilePath, CompilationUnit ast) {
        System.out.println("Scanning file " + sourceFilePath);
        ast.accept(astVisitor);
    }

}
