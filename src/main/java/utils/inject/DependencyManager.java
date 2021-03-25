package utils.inject;

import ast.ASTVisitor;
import ast.service.ContextService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class DependencyManager extends AbstractModule {

    @Override
    protected void configure() {
        bind(ContextService.class).in(Scopes.SINGLETON);
        bind(ASTVisitor.class).in(Scopes.SINGLETON);
    }

}
