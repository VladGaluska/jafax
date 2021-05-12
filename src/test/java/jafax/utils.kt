package jafax

import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Container
import jafax.AttributeGenerator.generateEmptyAttribute
import jafax.ClassGenerator.generateEmptyClass
import jafax.MethodGenerator.generateEmptyMethod
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.repository.model.Attribute.AttributeKind
import org.vladg.jafax.repository.model.Method
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals

fun encodeAstObject(astObject: ASTObject) =
    LayoutFormat.format.encodeToJsonElement(astObject).jsonObject

fun fillContainer(container: Container): Container {
    container.addToContainedClasses(generateEmptyClass())
    container.addToContainedMethods(generateEmptyMethod())
    container.addToCalledMethods(generateEmptyMethod())
    container.addToAccessedFields(generateEmptyAttribute())
    container.addToContainedAttributes(generateEmptyAttribute(AttributeKind.Parameter))
    container.addToContainedAttributes(generateEmptyAttribute(AttributeKind.LocalVariable))
    return container
}

fun getSimpleProjectPath(): Path = Paths.get("src/test/resources/org1")

fun getComplexProjectPath(): Path = Paths.get("src/test/resources/insider")

fun mapObjectsToNameSet(objects: Collection<ASTObject>)=
        objects.map { it.name }.toSet()

fun mapMethodsToSignatureSet(methods: Collection<Method>) =
        methods.map { it.signature }.toSet()

fun setEquals(set1: Set<String>, set2: Set<String>) {
    val valuesInSet1NotInSet2 = set1.toList().filter { !set2.contains(it) }.count()
    val valuesInSet2NotInSet1 = set2.toList().filter { !set1.contains(it) }.count()
    assertEquals(0, valuesInSet1NotInSet2, "Sets $set1 and $set2 are not equal")
    assertEquals(0, valuesInSet2NotInSet1, "Sets $set1 and $set2 are not equal")
}