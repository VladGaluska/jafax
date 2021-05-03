package org.vladg.jafax.io.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Attribute.AttributeKind
import org.vladg.jafax.io.serializers.decoder.AstDecoder
import org.vladg.jafax.io.serializers.decoder.CollectionDecoder
import org.vladg.jafax.io.serializers.encoder.CollectionEncoder
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.repository.model.Container

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Attribute::class)
class AttributeSerializer : KSerializer<Attribute> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Attribute") {
            element<Long>("id")
            element<String>("name")
            element<Long>("class")
            element("modifiers", listSerialDescriptor<String>())
            element<Long>("container")
            element<String>("kind")
        }

    override fun serialize(encoder: Encoder, value: Attribute) {
        val compositeEncoder = encoder.beginStructure(descriptor)
        val collectionEncoder = CollectionEncoder(compositeEncoder, descriptor)
        compositeEncoder.run {
            encodeLongElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.name)
            value.type?.let { encodeLongElement(descriptor, 2, it.id) }
            collectionEncoder.encodeModifiers(value.modifiers, 3)
            value.container?.let { encodeLongElement(descriptor, 4, it.id) }
            encodeStringElement(descriptor, 5, value.kind.toString())
            endStructure(descriptor)
        }
    }

    override fun deserialize(decoder: Decoder): Attribute {
        val attribute = Attribute()
        val compositeDecoder = decoder.beginStructure(descriptor)
        val collectionDecoder = CollectionDecoder(compositeDecoder, descriptor)
        attribute.id = compositeDecoder.decodeLongElement(descriptor, 0)
        attribute.name = compositeDecoder.decodeStringElement(descriptor, 1)
        AstDecoder.addObjectOrAddForUpdate(compositeDecoder.decodeLongElement(descriptor, 2)) {
            attribute.type = it as Class
        }
        attribute.modifiers = collectionDecoder.decodeModifiers(3)
        AstDecoder.addObjectOrAddForUpdate(compositeDecoder.decodeLongElement(descriptor, 4)) {
            attribute.container = it as Container
        }
        attribute.kind = AttributeKind.valueOf(compositeDecoder.decodeStringElement(descriptor, 5))
        CommonRepository.addObject(attribute)
        return attribute
    }

}