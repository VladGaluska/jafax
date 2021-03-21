package ast;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class ASTVisitor extends org.eclipse.jdt.core.dom.ASTVisitor {

    private static ASTVisitor currentInstance;

    private ASTVisitor() {
    }

    public static ASTVisitor getInstance() {
        if (currentInstance == null) {
            currentInstance = new ASTVisitor();
        }
        return currentInstance;
    }

    @Override
    public boolean visit(TypeDeclaration typeDeclaration) {
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

}
