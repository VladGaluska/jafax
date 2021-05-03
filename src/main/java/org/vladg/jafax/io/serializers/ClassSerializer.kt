package org.vladg.jafax.io.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Method
import org.vladg.jafax.io.serializers.decoder.AstDecoder
import org.vladg.jafax.io.serializers.decoder.CollectionDecoder
import org.vladg.jafax.io.serializers.encoder.CollectionEncoder
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.model.Container

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Class::class)
class ClassSerializer : KSerializer<Class> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Class") {
            element<Long>("id")
            element<String>("name")
            element<String>("fileName")
            element<Boolean>("isInterface")
            element("modifiers", listSerialDescriptor<String>())
            element<Long>("container")
            element<Long>("superClass")
            element("interfaces", listSerialDescriptor<Long>())
            element<Boolean>("isExternal")
            element("containedFields", listSerialDescriptor<Long>())
            element("containedClasses", listSerialDescriptor<Long>())
            element("containedMethods", listSerialDescriptor<Long>())
            element("accessedFields", listSerialDescriptor<Long>())
            element("calledMethods", listSerialDescriptor<Long>())
        }

    override fun serialize(encoder: Encoder, value: Class) {
        val compositeEncoder = encoder.beginStructure(descriptor)
        val collectionEncoder = CollectionEncoder(compositeEncoder, descriptor)
        compositeEncoder.run {
            encodeLongElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.name)
            value.fileName?.let { encodeStringElement(descriptor, 2, it) }
            if (value.isInterface) {
                encodeBooleanElement(descriptor, 3, true)
            }
            value.container?.let { encodeLongElement(descriptor, 5, it.id) }
            value.superClass?.let { encodeLongElement(descriptor, 6, it.id) }
            if (value.isExternal) {
                encodeBooleanElement(descriptor, 8, true)
            }
            encodeCollections(value, collectionEncoder)
            endStructure(descriptor)
        }
    }

    private fun encodeCollections(value: Class, collectionEncoder: CollectionEncoder) {
        collectionEncoder.encodeModifiers(value.modifiers, 4)
        collectionEncoder.encodeAstCollectionsByIndex(mapOf(
            7 to value.superInterfaces,
            9 to value.containedFields,
            10 to value.containedClasses,
            11 to value.containedMethods,
            12 to value.accessedFields,
            13 to value.calledMethods
        ))
    }

    override fun deserialize(decoder: Decoder): Class {
        val clazz = Class()
        val compositeDecoder = decoder.beginStructure(descriptor)
        val collectionDecoder = CollectionDecoder(compositeDecoder, descriptor)
        clazz.id = compositeDecoder.decodeLongElement(descriptor, 0)
        clazz.name = compositeDecoder.decodeStringElement(descriptor, 1)
        clazz.fileName = compositeDecoder.decodeStringElement(descriptor, 2)
        clazz.isInterface = compositeDecoder.decodeBooleanElement(descriptor, 3)
        AstDecoder.addObjectOrAddForUpdate(compositeDecoder.decodeLongElement(descriptor, 5)) {
            clazz.container = it as Container
        }
        AstDecoder.addObjectOrAddForUpdate(compositeDecoder.decodeLongElement(descriptor, 6)) {
            clazz.superClass = it as Class
        }
        clazz.isExternal = compositeDecoder.decodeBooleanElement(descriptor, 8)
        this.decodeCollections(clazz, collectionDecoder)
        ClassRepository.addClass(clazz)
        return clazz
    }

    private fun decodeCollections(clazz: Class, collectionDecoder: CollectionDecoder) {
        clazz.modifiers = collectionDecoder.decodeModifiers(4)
        collectionDecoder.decodeAstCollectionsByIndex(mapOf(
            7 to { clazz.addToInterfaces(it as Class) },
            9 to { clazz.addToContainedAttributes(it as Attribute) },
            10 to { clazz.addToContainedClasses(it as Class) },
            11 to { clazz.addToContainedMethods(it as Method) },
            12 to { clazz.addToAccessedFields(it as Attribute) },
            13 to { clazz.addToCalledMethods(it as Method) }
        ))
    }

}