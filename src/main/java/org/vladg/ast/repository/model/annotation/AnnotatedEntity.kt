package org.vladg.ast.repository.model.annotation

import org.vladg.ast.repository.model.ASTObject


abstract class AnnotatedEntity : ASTObject() {

    var annotationList: MutableList<Annotation> = ArrayList()

    fun addAnnotation(annotation: Annotation) {
        annotationList.add(annotation)
    }

}