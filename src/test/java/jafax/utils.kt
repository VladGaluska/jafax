package jafax

import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.container.Container
import jafax.AttributeGenerator.generateEmptyAttribute
import jafax.ClassGenerator.generateEmptyClass
import jafax.MethodGenerator.generateEmptyMethod
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.repository.model.Attribute.AttributeKind
import org.vladg.jafax.repository.model.container.Method
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun encodeAstObject(astObject: ASTObject) =
    LayoutFormat.format.encodeToJsonElement(astObject).jsonObject

fun fillContainer(container: Container): Container {
    container.addToContainedClasses(generateEmptyClass())
    container.addToContainedMethods(generateEmptyMethod())
    container.addToInvocations(generateEmptyMethod())
    container.addToAccesses(generateEmptyAttribute())
    container.addToContainedAttributes(generateEmptyAttribute(AttributeKind.Parameter))
    container.addToContainedAttributes(generateEmptyAttribute(AttributeKind.LocalVariable))
    return container
}

fun getSimpleProjectPath(): Path = Paths.get("src/test/resources/org1")

fun getComplexProjectPath(): Path = Paths.get("src/test/resources/insider")

fun mapObjectsToNameList(objects: Collection<ASTObject>)=
        objects.map { it.name }.toList()

fun mapMethodsToSignatureList(methods: Collection<Method>) =
        methods.map { it.signature }.toList()

fun collectionEquals(collection1: Collection<*>, collection2: Collection<*>) {
    assertEquals(collection1.size, collection2.size, "Expected: $collection1 actual: $collection2!")
    assertTrue(collection1.containsAll(collection2), "Expected: $collection1 actual: $collection2!")
    assertTrue(collection2.containsAll(collection1), "Expected: $collection1 actual: $collection2!")
}