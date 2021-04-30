package org.vladg.jafax.io.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.vladg.jafax.ast.repository.model.Attribute
import org.vladg.jafax.ast.repository.model.Class
import org.vladg.jafax.ast.repository.model.Method
import org.vladg.jafax.io.serializers.decoder.AstDecoder
import org.vladg.jafax.io.serializers.decoder.CollectionDecoder
import org.vladg.jafax.io.serializers.encoder.CollectionEncoder
import org.vladg.jafax.utils.extensions.stringToModifiers

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
            encodeBooleanElement(descriptor, 3, value.isInterface)
            value.superClass?.let { encodeLongElement(descriptor, 5, it.id) }
            if (value.isExternal) {
                encodeBooleanElement(descriptor, 7, value.isExternal)
            }
            encodeCollections(value, collectionEncoder)
            endStructure(descriptor)
        }
    }

    private fun encodeCollections(value: Class, collectionEncoder: CollectionEncoder) {
        collectionEncoder.encodeModifiers(value.modifiers, 4)
        collectionEncoder.encodeAstCollectionsByIndex(mapOf(
            6 to value.superInterfaces,
            8 to value.containedFields,
            9 to value.containedClasses,
            10 to value.containedMethods,
            11 to value.accessedFields,
            12 to value.calledMethods
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
            clazz.superClass = it as Class
        }
        clazz.isExternal = compositeDecoder.decodeBooleanElement(descriptor, 7)
        this.decodeCollections(clazz, collectionDecoder)
        return clazz
    }

    private fun decodeCollections(clazz: Class, collectionDecoder: CollectionDecoder) {
        clazz.modifiers = collectionDecoder.decodeModifiers(4)
        collectionDecoder.decodeAstCollectionsByIndex(mapOf(
            6 to { clazz.addToInterfaces(it as Class) },
            8 to { clazz.addToContainedAttributes(it as Attribute) },
            9 to { clazz.addToContainedClasses(it as Class) },
            10 to { clazz.addToContainedMethods(it as Method) },
            11 to { clazz.addToAccessedFields(it as Attribute) },
            12 to { clazz.addToCalledMethods(it as Method) }
        ))
    }

}