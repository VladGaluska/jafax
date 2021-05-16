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
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Method

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Method::class)
class MethodSerializer : ContainerSerializer<Method>() {

    override val layoutPositions = linkedMapOf(
        "id" to 0,
        "name" to 1,
        "signature" to 2,
        "isConstructor" to 3,
        "isDefaultConstructor" to 4,
        "returnType" to 5,
        "cyclomaticComplexity" to 6,
        "modifiers" to 7,
        "container" to 8,
        "parameters" to 9,
        "localVariables" to 10,
        "containedClasses" to 11,
        "containedMethods" to 12,
        "accessedFields" to 13,
        "calledMethods" to 14,
        "typeParameters" to 15
    )

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Method") {
            layoutPositions.forEach { (name, _) ->
                when (name) {
                    "id" -> element<Long>("id")
                    "name" -> element<String>("name")
                    "signature" -> element<String>("signature")
                    "isConstructor" -> element<Boolean>("isConstructor")
                    "isDefaultConstructor" -> element<Boolean>("isDefaultConstructor")
                    "returnType" -> element<Long>("returnType")
                    "cyclomaticComplexity" -> element<Int>("cyclomaticComplexity")
                    "modifiers" -> element("modifiers", listSerialDescriptor<String>())
                    "container" -> element<Long>("container")
                    "parameters" -> element("parameters", listSerialDescriptor<Long>())
                    "localVariables" -> element("localVariables", listSerialDescriptor<Long>())
                    "containedClasses" -> element("containedClasses", listSerialDescriptor<Long>())
                    "containedMethods" -> element("containedMethods", listSerialDescriptor<Long>())
                    "accessedFields" -> element("accessedFields", listSerialDescriptor<Long>())
                    "calledMethods" -> element("calledMethods", listSerialDescriptor<Long>())
                    "typeParameters" -> element("typeParameters", listSerialDescriptor<Long>())
                }
            }
        }

    override fun createObject(): Method = Method()

    override fun encodeExtraProperties(
        compositeEncoder: CompositeEncoder,
        collectionEncoder: CollectionEncoder,
        value: Method
    ) {
        super.encodeExtraProperties(compositeEncoder, collectionEncoder, value)
        compositeEncoder.encodeStringElement(descriptor, getIndex("signature"), value.signature)
        if (value.isConstructor) {
            compositeEncoder.encodeBooleanElement(descriptor, getIndex("isConstructor"), true)
        }
        compositeEncoder.encodeIntElement(descriptor, getIndex("cyclomaticComplexity"), value.cyclomaticComplexity)
        value.returnType?.let { compositeEncoder.encodeLongElement(descriptor, getIndex("returnType"), it.id) }
        if (value.isDefaultConstructor) {
            compositeEncoder.encodeBooleanElement(descriptor, getIndex("isDefaultConstructor"), true)
        }
        collectionEncoder.encodeAstCollectionsByIndex(mapOf(
            getIndex("parameters") to value.parameters,
            getIndex("localVariables") to value.localVariables,
        ))
    }

    override fun decodeIndex(
        index: Int,
        compositeDecoder: CompositeDecoder,
        collectionDecoder: CollectionDecoder,
        obj: Method
    ) {
        when(index) {
            getIndex("signature") -> obj.signature = compositeDecoder.decodeStringElement(descriptor, index)
            getIndex("isConstructor") -> obj.isConstructor = compositeDecoder.decodeBooleanElement(descriptor, index)
            getIndex("isDefaultConstructor") -> obj.isDefaultConstructor = compositeDecoder.decodeBooleanElement(descriptor, index)
            getIndex("cyclomaticComplexity") -> obj.cyclomaticComplexity = compositeDecoder.decodeIntElement(descriptor, index)
            getIndex("returnType") -> AstDecoder.addObjectOrAddForUpdate(compositeDecoder.decodeLongElement(descriptor, index)) {
                obj.returnType = it as Class
            }
            getIndex("parameters"), getIndex("localVariables") -> collectionDecoder.decodeAstCollection(index) {
                obj.addToContainedAttributes(it as Attribute)
            }
            else -> super.decodeIndex(index, compositeDecoder, collectionDecoder, obj)
        }
    }

}