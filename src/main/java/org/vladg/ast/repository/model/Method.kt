package org.vladg.ast.repository.model

import org.vladg.ast.repository.model.annotation.AnnotatedEntity

data class Method(val name: String) : AnnotatedEntity(), Container