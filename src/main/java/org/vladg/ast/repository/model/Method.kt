package org.vladg.ast.repository.model;

import org.vladg.ast.repository.model.annotation.AnnotatedEntity;
import lombok.Builder;

@Builder
public class Method extends AnnotatedEntity implements Container{

    public String name;

}
