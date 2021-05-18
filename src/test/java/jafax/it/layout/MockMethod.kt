package jafax.it.layout

import jafax.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Method
import org.vladg.jafax.repository.model.Modifier
import kotlin.test.assertEquals

@Serializable
@SerialName("MockMethod")
class MockMethod(
        var name: String,
        var signature: String,
        var parameterNames: Set<String>,
        var localVariableNames: Set<String>,
        var containedMethodSignatures: Set<String>,
        var calledMethodSignatures: Set<String>,
        var accessedFieldNames: Set<String>,
        var containedClassNames: Set<String>,
        var typeParameterNames: Set<String>,
        var modifiers: Set<String> = HashSet(),
        var isConstructor: Boolean,
        var returnTypeName: String,
        var cyclomaticComplexity: Int,
        var containerName: String? = null
): Mock() {

    fun verify(method: Method) {
        assertEquals(name, method.name)
        assertEquals(signature, method.signature)
        collectionEquals(parameterNames, mapObjectsToNameList(method.parameters))
        collectionEquals(localVariableNames, mapObjectsToNameList(method.localVariables))
        collectionEquals(containedMethodSignatures, mapMethodsToSignatureList(method.containedMethods))
        assertEquals(modifiers.map { Modifier.valueOf(it) }.toSet(), method.modifiers)
        collectionEquals(calledMethodSignatures, mapMethodsToSignatureList(method.calledMethods))
        collectionEquals(accessedFieldNames, mapObjectsToNameList(method.accessedFields))
        collectionEquals(containedClassNames, mapObjectsToNameList(method.containedClasses))
        collectionEquals(typeParameterNames, mapObjectsToNameList(method.typeParameters.filterNotNull()))
        assertEquals(isConstructor, method.isConstructor)
        assertEquals(returnTypeName, method.returnType?.name)
        assertEquals(cyclomaticComplexity, method.cyclomaticComplexity)
        assertEquals(containerName, method.container?.name)
    }

    override fun isSame(obj: ASTObject) =
            obj is Method &&
            signature == obj.signature &&
            containerName == obj.container?.name

}