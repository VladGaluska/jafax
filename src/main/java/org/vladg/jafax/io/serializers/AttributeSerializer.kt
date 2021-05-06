package org.vladg.jafax.io.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import org.vladg.jafax.io.serializers.decoder.AstDecoder
import org.vladg.jafax.io.serializers.decoder.CollectionDecoder
import org.vladg.jafax.io.serializers.encoder.CollectionEncoder
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Attribute.AttributeKind
import org.vladg.jafax.repository.model.Class

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Attribute::class)
class AttributeSerializer : ASTSerializer<Attribute>() {

    override val layoutPositions = linkedMapOf(
        "id" to 0,
        "name" to 1,
        "class" to 2,
        "modifiers" to 3,
        "container" to 4,
        "kind" to 5
    ).toList().sortedBy { (_, value) -> value }.toMap()

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Attribute") {
            layoutPositions.forEach { (name, _) ->
                when(name) {
                    "id" -> element<Long>("id")
                    "name" -> element<String>("name")
                    "class" -> element<Long>("class")
                    "modifiers" -> element("modifiers", listSerialDescriptor<String>())
                    "container" -> element<Long>("container")
                    "kind" -> element<String>("kind")
                }
            }
        }

    override fun createObject(): Attribute = Attribute()

    override fun encodeExtraProperties(
        compositeEncoder: CompositeEncoder,
        collectionEncoder: CollectionEncoder,
        value: Attribute
    ) {
        value.type?.let { compositeEncoder.encodeLongElement(descriptor, getIndex("class"), it.id) }
        compositeEncoder.encodeStringElement(descriptor, getIndex("kind"), value.kind.toString())
    }

    override fun decodeIndex(
        index: Int,
        compositeDecoder: CompositeDecoder,
        collectionDecoder: CollectionDecoder,
        obj: Attribute
    ) {
        when(index) {
            getIndex("class") -> AstDecoder.addObjectOrAddForUpdate(compositeDecoder.decodeLongElement(descriptor, index)) {
                obj.type = it as Class
            }
            getIndex("kind") -> obj.kind = AttributeKind.valueOf(compositeDecoder.decodeStringElement(descriptor, index))
        }
    }

}