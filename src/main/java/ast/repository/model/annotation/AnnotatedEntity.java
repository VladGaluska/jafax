package ast.repository.model.annotation;

import ast.repository.model.ASTObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class AnnotatedEntity extends ASTObject {

    @Getter
    public List<Annotation> annotationList = new ArrayList<>();

    public void addAnnotation(Annotation annotation) {
        this.annotationList.add(annotation);
    }

}
