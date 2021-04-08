package org.vladg.jafax.utils.inject;

import ast.ASTVisitor;
import ast.repository.NonPersistentRepository;
import ast.repository.model.Class;
import ast.repository.model.File;
import ast.repository.model.Method;
import ast.service.FileService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

public class DependencyManager extends AbstractModule {

    @Override
    protected void configure() {
        bind(ASTVisitor.class).in(Scopes.SINGLETON);
        bind(FileService.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<NonPersistentRepository<File>>() {}).in(Scopes.SINGLETON);
        bind(new TypeLiteral<NonPersistentRepository<Method>>() {}).in(Scopes.SINGLETON);
        bind(new TypeLiteral<NonPersistentRepository<Class>>() {}).in(Scopes.SINGLETON);
    }

}
