package org.vladg.jafax.io.serializers.encoder

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import org.vladg.jafax.ast.repository.model.ASTObject
import org.vladg.jafax.ast.repository.model.Modifier

class CollectionEncoder(
    private val compositeEncoder: CompositeEncoder,
    private val descriptor: SerialDescriptor
) {

    private fun encodeAstCollection(toEncode: Collection<ASTObject>, index: Int) =
        encodeCollection(toEncode.map { it.id }, index, Long.serializer())

    fun encodeAstCollectionsByIndex(astCollectionsByIndex: Map<Int, Collection<ASTObject>>) =
        astCollectionsByIndex.forEach { (key, value) -> this.encodeAstCollection(value, key) }

    private fun encodeStringList(toEncode: List<String>, index: Int) =
        encodeCollection(toEncode, index, String.serializer())

    fun encodeModifiers(toEncode: Set<Modifier>, index: Int) =
        encodeStringList(toEncode.map { it.toString() }, index)

    private inline fun <reified T> encodeCollection(
        toEncode: List<T>,
        index: Int,
        serializer: KSerializer<T>
    ) {
        if (toEncode.isNotEmpty()) {
            compositeEncoder.encodeSerializableElement(
                descriptor,
                index,
                ListSerializer(serializer),
                toEncode
            )
        }
    }

}