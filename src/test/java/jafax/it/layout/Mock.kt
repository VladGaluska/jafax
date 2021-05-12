package jafax.it.layout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.vladg.jafax.repository.model.ASTObject

@Serializable
@SerialName("Mock")
abstract class Mock {

    abstract fun isSame(obj: ASTObject): Boolean

}