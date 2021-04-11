package org.vladg.ast.repository.model

data class File(val name: String, val packageName: String? = null,
                var imports: List<String> = ArrayList(), val containedClasses: List<Class> = ArrayList())
    : ASTObject(), Container