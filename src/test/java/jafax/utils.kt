package jafax

import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.vladg.jafax.io.LayoutFormat
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Container
import jafax.AttributeGenerator.generateEmptyAttribute
import jafax.ClassGenerator.generateEmptyClass
import jafax.MethodGenerator.generateEmptyMethod
import org.vladg.jafax.repository.model.Attribute.AttributeKind
import java.nio.file.Path
import java.nio.file.Paths

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

fun getSimpleProjectPath(): Path = Paths.get("src/test/resources/org")

fun getComplexProjectPath(): Path = Paths.get("src/test/resources/insider")