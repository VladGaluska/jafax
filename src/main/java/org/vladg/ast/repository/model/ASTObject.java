package org.vladg.ast.repository.model;

public abstract class ASTObject {

    private static long lastId = 0;

    public long id;

    public ASTObject() {
        this.id = lastId ++;
    }

}
