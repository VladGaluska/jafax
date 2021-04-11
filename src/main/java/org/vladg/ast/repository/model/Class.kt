package org.vladg.ast.repository.model;

import org.vladg.ast.repository.model.annotation.AnnotatedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Class extends AnnotatedEntity implements Container{

    public String name;

    public Container container;

    public List<Class> containedClasses = new ArrayList<>();

    public List<Method> containedMethods = new ArrayList<>();

    public List<Field> containedFields = new ArrayList<>();

    public void addContainedClass(Class c) {
        this.containedClasses.add(c);
    }

    public void addContainedMethod(Method m) {
        this.containedMethods.add(m);
    }

    public void addContainedField(Field f) {
        this.containedFields.add(f);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Class) {
            var toCheck = (Class) o;
            return this.container.equals(toCheck.container) &&
                   this.name.equals(toCheck.name);
        }
        return false;
    }

}
