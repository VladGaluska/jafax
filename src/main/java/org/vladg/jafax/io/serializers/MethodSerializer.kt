package org.vladg.jafax.io.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.vladg.jafax.io.serializers.decoder.AstDecoder
import org.vladg.jafax.io.serializers.decoder.CollectionDecoder
import org.vladg.jafax.io.serializers.encoder.CollectionEncoder
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Method

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Method::class)
class MethodSerializer : KSerializer<Method> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Method") {
            element<Long>("id")
            element<String>("name")
            element<String>("signature")
            element<Boolean>("isConstructor")
            element<Long>("returnType")
            element("modifiers", listSerialDescriptor<String>())
            element<Long>("container")
            element("parameters", listSerialDescriptor<Long>())
            element("localVariables", listSerialDescriptor<Long>())
            element("containedClasses", listSerialDescriptor<Long>())
            element("containedMethods", listSerialDescriptor<Long>())
            element("accessedFields", listSerialDescriptor<Long>())
            element("calledMethods", listSerialDescriptor<Long>())
        }

    override fun serialize(encoder: Encoder, value: Method) {
        val compositeEncoder = encoder.beginStructure(descriptor)
        val collectionEncoder = CollectionEncoder(compositeEncoder, descriptor)
        compositeEncoder.run {
            encodeLongElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.name)
            encodeStringElement(descriptor, 2, value.signature)
            if (value.isConstructor) {
                encodeBooleanElement(descriptor, 3, true)
            }
            value.returnType?.let { encodeLongElement(descriptor, 4, it.id) }
            value.container?.let { encodeLongElement(descriptor, 6, it.id) }
            encodeCollections(value, collectionEncoder)
            endStructure(descriptor)
        }
    }

    private fun encodeCollections(value: Method, collectionEncoder: CollectionEncoder) {
        collectionEncoder.encodeModifiers(value.modifiers, 5)
        collectionEncoder.encodeAstCollectionsByIndex(mapOf(
            7 to value.parameters,
            8 to value.localVariables,
            9 to value.containedClasses,
            10 to value.containedMethods,
            11 to value.accessedFields,
            12 to value.calledMethods
        ))
    }

    override fun deserialize(decoder: Decoder): Method {
        val method = Method()//TODO: remove duplicate code
        val compositeDecoder = decoder.beginStructure(descriptor)
        val collectionDecoder = CollectionDecoder(compositeDecoder, descriptor)
        method.id = compositeDecoder.decodeLongElement(descriptor, 0)
        method.name = compositeDecoder.decodeStringElement(descriptor, 1)
        method.signature = compositeDecoder.decodeStringElement(descriptor, 2)
        method.isConstructor = compositeDecoder.decodeBooleanElement(descriptor, 3)
        AstDecoder.addObjectOrAddForUpdate(compositeDecoder.decodeLongElement(descriptor, 4)) {
            method.returnType = it as Class
        }
        AstDecoder.addObjectOrAddForUpdate(compositeDecoder.decodeLongElement(descriptor, 6)) {
            method.container = it as Class
        }
        this.decodeCollections(method, collectionDecoder)
        CommonRepository.addObject(method)
        return method
    }

    private fun decodeCollections(method: Method, collectionDecoder: CollectionDecoder) {
        method.modifiers = collectionDecoder.decodeModifiers(5)
        collectionDecoder.decodeAstCollectionsByIndex(mapOf(
            7 to { method.addToContainedAttributes(it as Attribute) },
            8 to { method.addToContainedAttributes(it as Attribute) },
            9 to { method.addToContainedClasses(it as Class) },
            10 to { method.addToContainedMethods(it as Method) },
            11 to { method.addToAccessedFields(it as Attribute) },
            12 to { method.addToCalledMethods(it as Method) }
        ))
    }

}