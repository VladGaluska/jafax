package AST;

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

}
