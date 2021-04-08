package org.vladg.jafax.utils.inject;

import org.vladg.ast.ASTVisitor;
import org.vladg.ast.repository.NonPersistentRepository;
import org.vladg.ast.repository.model.Class;
import org.vladg.ast.repository.model.File;
import org.vladg.ast.repository.model.Method;
import org.vladg.ast.service.FileService;
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
