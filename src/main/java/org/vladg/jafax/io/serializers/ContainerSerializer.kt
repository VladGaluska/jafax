/*
package org.vladg.jafax.io.serializers

import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import org.vladg.jafax.io.serializers.decoder.CollectionDecoder
import org.vladg.jafax.io.serializers.encoder.CollectionEncoder
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.container.Class
import org.vladg.jafax.repository.model.container.Container
import org.vladg.jafax.repository.model.container.Method

abstract class ContainerSerializer<T: Container>: ASTSerializer<T>() {

    override fun encodeExtraProperties(
        compositeEncoder: CompositeEncoder,
        collectionEncoder: CollectionEncoder,
        value: T
    ) {
        collectionEncoder.encodeAstCollectionsByIndex(
            mapOf(
                getIndex("containedClasses") to value.containedClasses,
                getIndex("containedMethods") to value.containedMethods,
                getIndex("accessedFields") to value.accesses,
                getIndex("calledMethods") to value.invocations,
                getIndex("typeParameters") to value.typeParameters.filterNotNull()
            )
        )
    }

    override fun decodeIndex(
        index: Int,
        compositeDecoder: CompositeDecoder,
        collectionDecoder: CollectionDecoder,
        obj: T
    ) {
        when(index) {
            getIndex("containedClasses") -> collectionDecoder.decodeAstCollection(index) {
                obj.addToContainedClasses(it as Class)
            }
            getIndex("containedMethods") -> collectionDecoder.decodeAstCollection(index) {
                obj.addToContainedMethods(it as Method)
            }
            getIndex("accessedFields") -> collectionDecoder.decodeAstCollection(index) {
                obj.addToAccesses(it as Attribute)
            }
            getIndex("calledMethods") -> collectionDecoder.decodeAstCollection(index) {
                obj.addToInvocations(it as Method)
            }
            getIndex("typeParameters") -> collectionDecoder.decodeAstCollection(index) {
                obj.addToTypeParameters(it as Class)
            }
        }
    }

}*/
