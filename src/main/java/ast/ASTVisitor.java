package ast;

import java.util.ArrayList;
import java.util.List;

public class ASTVisitor extends org.eclipse.jdt.core.dom.ASTVisitor {

    private static ASTVisitor currentInstance;

    private final List<String> classNames = new ArrayList<>();

    private ASTVisitor() {
    }

    public static ASTVisitor getInstance() {
        if (currentInstance == null) {
            currentInstance = new ASTVisitor();
        }
        return currentInstance;
    }

}
