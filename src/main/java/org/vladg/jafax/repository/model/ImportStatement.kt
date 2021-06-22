package org.vladg.jafax.repository.model

import kotlinx.serialization.Serializable
import org.vladg.jafax.io.serializers.ImportStatementSerializer

@Serializable(with = ImportStatementSerializer::class)
class ImportStatement(
    var importedClass: String = "",
    static: Boolean = false,
    var onDemand: Boolean = false
) : ASTObject("", if (static) setOf(Modifier.Static) else emptySet(), null)
