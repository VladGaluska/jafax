package ast.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class File extends ASTObject implements Container{

    public String name;

    public String packageName;

    public List<String> imports;

    public List<Class> containedClasses;

    public void addClass(Class clazz) {
        this.containedClasses.add(clazz);
    }

    public void addImport(String imported) {
        this.imports.add(imported);
    }

}
