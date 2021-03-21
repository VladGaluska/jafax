package ast.repository;

import ast.repository.model.Class;

public class ClassRepository implements NonPersistentRepository<Class> {

    @Override
    public Class findById(Long id) {
        return null;
    }

}
