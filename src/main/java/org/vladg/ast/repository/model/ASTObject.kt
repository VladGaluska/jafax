package org.vladg.ast.repository.model

abstract class ASTObject {

    var id: Long = lastId++

    companion object {
        private var lastId: Long = 0
    }

}