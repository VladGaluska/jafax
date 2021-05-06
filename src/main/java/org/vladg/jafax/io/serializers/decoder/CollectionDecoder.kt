package org.vladg.jafax.io.serializers.decoder

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Modifier
import org.vladg.jafax.utils.extensions.stringToModifiers

class CollectionDecoder(
    private val compositeDecoder: CompositeDecoder,
    private val descriptor: SerialDescriptor
) {

    fun decodeAstCollection(index: Int, adder: (ASTObject) -> Unit) =
        AstDecoder.addObjectsOrAddForUpdate(
            decodeCollection(index, Long.serializer()),
            adder
        )

    private fun decodeStringCollection(index: Int): List<String> =
        decodeCollection(index, String.serializer())

    fun decodeModifiers(index: Int): Set<Modifier> =
        stringToModifiers(decodeStringCollection(index))

    private inline fun <reified T> decodeCollection(
        index: Int,
        deserializer: KSerializer<T>
    ): List<T> =
        compositeDecoder.decodeSerializableElement(
            descriptor,
            index,
            ListSerializer(deserializer)
        )

}