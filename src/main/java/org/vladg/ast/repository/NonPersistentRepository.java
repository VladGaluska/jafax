package org.vladg.ast.repository;

import org.vladg.ast.repository.model.ASTObject;
import org.vladg.ast.repository.model.Container;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class NonPersistentRepository<T extends ASTObject> {

    private final Map<Long, T> objects = new HashMap<>();

    private static final Stack<Container> containersSaved = new Stack<>();

    public void addObject(T object) {
        if (object instanceof Container) {
            containersSaved.push((Container) object);
        }
        objects.put(object.id, object);
    }

    public T findById(Long id) {
        return objects.get(id);
    }

    public static Container popUntilInstance(Class<? extends Container> toCheck) {
        while (!containersSaved.isEmpty()) {
            var container = containersSaved.pop();
            if (container.getClass().equals(toCheck)) {
                return container;
            }
        }
        return null;
    }

}
