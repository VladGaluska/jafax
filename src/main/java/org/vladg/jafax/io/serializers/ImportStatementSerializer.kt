package org.vladg.jafax.io.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import org.vladg.jafax.io.serializers.decoder.CollectionDecoder
import org.vladg.jafax.io.serializers.encoder.CollectionEncoder
import org.vladg.jafax.repository.model.ImportStatement

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ImportStatement::class)
class ImportStatementSerializer : ASTSerializer<ImportStatement>() {
    override val layoutPositions = linkedMapOf(
        "id" to 0,
        "name" to 1,
        "modifiers" to 2,
        "importedClass" to 3,
        "onDemand" to 4,
        "container" to 5,
    ).toList().sortedBy { (_, value) -> value }.toMap()

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ImportStatement") {
            layoutPositions.forEach { (name, _) ->
                when (name) {
                    "id" -> element<Long>("id")
                    "name" -> element<String>("name")
                    "modifiers" -> element("modifiers", listSerialDescriptor<String>())
                    "importedClass" -> element<String>("importedClass")
                    "onDemand" -> element<Boolean>("onDemand")
                    "container" -> element<Long>("container")
                }
            }
        }


    override fun encodeExtraProperties(
        compositeEncoder: CompositeEncoder,
        collectionEncoder: CollectionEncoder,
        value: ImportStatement
    ) {
        compositeEncoder.encodeStringElement(descriptor, getIndex("importedClass"), value.importedClass)
        compositeEncoder.encodeBooleanElement(descriptor, getIndex("onDemand"), value.onDemand)
    }

    override fun decodeIndex(
        index: Int,
        compositeDecoder: CompositeDecoder,
        collectionDecoder: CollectionDecoder,
        obj: ImportStatement
    ) {
        when (index) {
            getIndex("importedClass") -> obj.importedClass = compositeDecoder.decodeStringElement(descriptor, index)
            getIndex("onDemand") -> obj.onDemand = compositeDecoder.decodeBooleanElement(descriptor, index)
        }
    }

    override fun createObject() = ImportStatement()
}
