package AST;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

public class ASTRequester extends FileASTRequestor {

    @Override
    public void acceptAST(String sourceFilePath, CompilationUnit ast) {
        System.out.println("Scanning file " + sourceFilePath);
        ast.accept(ASTVisitor.getInstance());
    }

}
