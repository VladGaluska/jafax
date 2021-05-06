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
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Class

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Class::class)
class ClassSerializer : ContainerSerializer<Class>() {

    override val layoutPositions = linkedMapOf(
        "id" to 0,
        "name" to 1,
        "fileName" to 2,
        "modifiers" to 3,
        "isInterface" to 4,
        "container" to 5,
        "superClass" to 6,
        "interfaces" to 7,
        "isExternal" to 8,
        "containedFields" to 9,
        "containedClasses" to 10,
        "containedMethods" to 11,
        "accessedFields" to 12,
        "calledMethods" to 13
    ).toList().sortedBy { (_, value) -> value }.toMap()

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Class") {
            layoutPositions.forEach { (name, _) ->
                when(name) {
                    "id" -> element<Long>("id")
                    "name" -> element<String>("name")
                    "fileName" -> element<String>("fileName")
                    "modifiers" -> element("modifiers", listSerialDescriptor<String>())
                    "isInterface" -> element<Boolean>("isInterface")
                    "container" -> element<Long>("container")
                    "superClass" -> element<Long>("superClass")
                    "interfaces" -> element("interfaces", listSerialDescriptor<Long>())
                    "isExternal" -> element<Boolean>("isExternal")
                    "containedFields" -> element("containedFields", listSerialDescriptor<Long>())
                    "containedClasses" -> element("containedClasses", listSerialDescriptor<Long>())
                    "containedMethods" -> element("containedMethods", listSerialDescriptor<Long>())
                    "accessedFields" -> element("accessedFields", listSerialDescriptor<Long>())
                    "calledMethods" -> element("calledMethods", listSerialDescriptor<Long>())
                }
            }
        }

    override fun createObject() = Class()

    override fun saveObject(obj: Class) =
        ClassRepository.addClass(obj)

    override fun encodeExtraProperties(
        compositeEncoder: CompositeEncoder,
        collectionEncoder: CollectionEncoder,
        value: Class
    ) {
        super.encodeExtraProperties(compositeEncoder, collectionEncoder, value)
        value.fileName?.let { compositeEncoder.encodeStringElement(descriptor, getIndex("fileName"), it) }
        if (value.isInterface) {
            compositeEncoder.encodeBooleanElement(descriptor, getIndex("isInterface"), true)
        }
        value.superClass?.let { compositeEncoder.encodeLongElement(descriptor, getIndex("superClass"), it.id) }
        if (value.isExternal) {
            compositeEncoder.encodeBooleanElement(descriptor, getIndex("isExternal"), true)
        }
        collectionEncoder.encodeAstCollectionsByIndex(mapOf(
            getIndex("interfaces") to value.superInterfaces,
            getIndex("containedFields") to value.containedFields
        ))
    }

    override fun decodeIndex(index: Int, compositeDecoder: CompositeDecoder, collectionDecoder: CollectionDecoder, obj: Class) {
        when(index){
            getIndex("fileName") -> obj.fileName = compositeDecoder.decodeStringElement(descriptor, index)
            getIndex("isInterfaces") -> obj.isInterface = compositeDecoder.decodeBooleanElement(descriptor, index)
            getIndex("superClass") -> AstDecoder.addObjectOrAddForUpdate(compositeDecoder.decodeLongElement(descriptor, index)) {
                obj.superClass = it as Class
            }
            getIndex("interfaces") -> collectionDecoder.decodeAstCollection(index) {
                obj.addToInterfaces(it as Class)
            }
            getIndex("isExternal") -> obj.isExternal = compositeDecoder.decodeBooleanElement(descriptor, index)
            getIndex("containedAttributes") -> collectionDecoder.decodeAstCollection(index) {
                obj.addToContainedAttributes(it as Attribute)
            }
            else -> super.decodeIndex(index, compositeDecoder, collectionDecoder, obj)
        }
    }
}