package ast;

import lombok.Getter;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ASTVisitor extends org.eclipse.jdt.core.dom.ASTVisitor {

    private static ASTVisitor currentInstance;

    @Getter
    private final List<String> classNames = new ArrayList<>();

    private ASTVisitor() {
    }

    public static ASTVisitor getInstance() {
        if (currentInstance == null) {
            currentInstance = new ASTVisitor();
        }
        return currentInstance;
    }

    public boolean visit(TypeDeclaration typeDeclaration) {
        classNames.add(typeDeclaration.getName().getIdentifier());
        return true;
    }

}
