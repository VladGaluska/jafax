package org.vladg.jafax.io

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.vladg.jafax.ast.repository.model.ASTObject

object ASTPropertySerializer : KSerializer<ASTObject> {

    override val descriptor = ASTObject.serializer().descriptor

    override fun deserialize(decoder: Decoder): ASTObject {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: ASTObject) {
        encoder.encodeLong(value.id)
    }

}