package ast.repository.model;

import ast.repository.model.annotation.AnnotatedEntity;
import lombok.Builder;

@Builder
public class Method extends AnnotatedEntity implements Container{

    public String name;

}
