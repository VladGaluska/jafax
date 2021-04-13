package org.vladg.ast.repository.model

import org.vladg.ast.repository.model.annotation.AnnotatedEntity

data class Class(
    val name: String,
    val container: Container,
    val containedClasses: List<Class> = ArrayList(),
    val containedMethods: List<Method> = ArrayList(),
    val containedFields: List<Field> = ArrayList())
    : AnnotatedEntity(), Container {

}