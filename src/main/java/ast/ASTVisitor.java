package ast;

import ast.service.ContextService;
import com.google.inject.Inject;
import org.eclipse.jdt.core.dom.*;

public class ASTVisitor extends org.eclipse.jdt.core.dom.ASTVisitor {

    @Inject
    private ContextService contextService;

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
        this.contextService.doSomething();
        System.out.println("Did something!");
        return true;
    }

}
