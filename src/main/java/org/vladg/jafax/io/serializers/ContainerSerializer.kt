package org.vladg.jafax.io.serializers

import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import org.vladg.jafax.io.serializers.decoder.CollectionDecoder
import org.vladg.jafax.io.serializers.encoder.CollectionEncoder
import org.vladg.jafax.repository.model.Attribute
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Container
import org.vladg.jafax.repository.model.Method

abstract class ContainerSerializer<T: Container>: ASTSerializer<T>() {

    override fun encodeExtraProperties(
        compositeEncoder: CompositeEncoder,
        collectionEncoder: CollectionEncoder,
        value: T
    ) {
        collectionEncoder.encodeAstCollectionsByIndex(
            mapOf(
                layoutPositions["containedClasses"]!! to value.containedClasses,
                layoutPositions["containedMethods"]!! to value.containedMethods,
                layoutPositions["accessedFields"]!! to value.accessedFields,
                layoutPositions["calledMethods"]!! to value.calledMethods
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
            layoutPositions["containedClasses"]!! -> collectionDecoder.decodeAstCollection(index) {
                obj.addToContainedClasses(it as Class)
            }
            layoutPositions["containedMethods"]!! -> collectionDecoder.decodeAstCollection(index) {
                obj.addToContainedMethods(it as Method)
            }
            layoutPositions["accessedFields"]!! -> collectionDecoder.decodeAstCollection(index) {
                obj.addToAccessedFields(it as Attribute)
            }
            layoutPositions["calledMethods"]!! -> collectionDecoder.decodeAstCollection(index) {
                obj.addToCalledMethods(it as Method)
            }
        }
    }

}