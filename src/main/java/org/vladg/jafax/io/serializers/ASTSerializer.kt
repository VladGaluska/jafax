package org.vladg.jafax.io.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.vladg.jafax.io.serializers.decoder.AstDecoder
import org.vladg.jafax.io.serializers.decoder.CollectionDecoder
import org.vladg.jafax.io.serializers.encoder.CollectionEncoder
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Container

abstract class ASTSerializer<T: ASTObject>: KSerializer<T> {

    abstract override val descriptor: SerialDescriptor

    abstract val layoutPositions: Map<String, Int>

    final override fun serialize(encoder: Encoder, value: T) {
        val compositeEncoder = encoder.beginStructure(descriptor)
        val collectionEncoder = CollectionEncoder(compositeEncoder, descriptor)
        encodeAstProperties(compositeEncoder, collectionEncoder, value)
        encodeExtraProperties(compositeEncoder, collectionEncoder, value)
        compositeEncoder.endStructure(descriptor)
    }

    private fun encodeAstProperties(
        compositeEncoder: CompositeEncoder,
        collectionEncoder: CollectionEncoder,
        value: T
    ) {
        compositeEncoder.encodeLongElement(descriptor, getIndex("id"), value.id)
        compositeEncoder.encodeStringElement(descriptor, getIndex("name"), value.name)
        collectionEncoder.encodeModifiers(value.modifiers, getIndex("modifiers"))
        value.container?.let {
            compositeEncoder.encodeLongElement(descriptor, getIndex("container"), it.id)
        }
    }

    protected abstract fun encodeExtraProperties(
        compositeEncoder: CompositeEncoder,
        collectionEncoder: CollectionEncoder,
        value: T
    )

    final override fun deserialize(decoder: Decoder): T {
        val value = createObject()
        val compositeDecoder = decoder.beginStructure(descriptor)
        val collectionDecoder = CollectionDecoder(compositeDecoder, descriptor)
        var index = compositeDecoder.decodeElementIndex(descriptor)
        while (index != CompositeDecoder.DECODE_DONE) {
            when(index) {
                getIndex("id") -> value.id = compositeDecoder.decodeLongElement(descriptor, index)
                getIndex("name") -> value.name = compositeDecoder.decodeStringElement(descriptor, index)
                getIndex("modifiers") -> value.modifiers = collectionDecoder.decodeModifiers(index)
                getIndex("container") -> AstDecoder.addObjectOrAddForUpdate(
                    compositeDecoder.decodeLongElement(descriptor, index)
                ) {
                    value.container = it as Container
                }
                else -> decodeIndex(index, compositeDecoder, collectionDecoder, value)
            }
            index = compositeDecoder.decodeElementIndex(descriptor)
        }
        compositeDecoder.endStructure(descriptor)
        saveObject(value)
        return value
    }

    protected abstract fun decodeIndex(
        index: Int,
        compositeDecoder: CompositeDecoder,
        collectionDecoder: CollectionDecoder,
        obj: T
    )

    protected abstract fun createObject(): T

    protected open fun saveObject(obj: T) {
        CommonRepository.addObject(obj)
        AstDecoder.doUpdate(obj)
    }

    protected fun getIndex(name: String) =
        layoutPositions[name]!!

}