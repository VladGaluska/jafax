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
import org.vladg.jafax.repository.FileRepository
import org.vladg.jafax.repository.model.File
import org.vladg.jafax.repository.model.ImportStatement

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = File::class)
class FileSerializer : ASTSerializer<File>() {
    override val layoutPositions = linkedMapOf(
        "id" to 0,
        "name" to 1,
        "modifiers" to 2,
        "imports" to 3,
        "container" to 4,
    ).toList().sortedBy { (_, value) -> value }.toMap()

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("File") {
            layoutPositions.forEach { (name, _) ->
                when (name) {
                    "id" -> element<Long>("id")
                    "name" -> element<String>("name")
                    "modifiers" -> element("modifiers", listSerialDescriptor<String>())
                    "imports" -> element("imports", listSerialDescriptor<Long>())
                    "container" -> element<Long>("container")
                }
            }
        }

    override fun encodeExtraProperties(
        compositeEncoder: CompositeEncoder,
        collectionEncoder: CollectionEncoder,
        value: File
    ) {
        collectionEncoder.encodeAstCollectionsByIndex(
            mapOf(
                getIndex("imports") to value.imports,
            )
        )
    }

    override fun decodeIndex(
        index: Int,
        compositeDecoder: CompositeDecoder,
        collectionDecoder: CollectionDecoder,
        obj: File
    ) {
        when (index) {
            getIndex("imports") -> collectionDecoder.decodeAstCollection(index) {
                obj.addImport(it as ImportStatement)
            }
        }
    }

    override fun createObject() = File()

    override fun saveObject(obj: File) {
        super.saveObject(obj)
        FileRepository.addObject(obj)
    }
}
