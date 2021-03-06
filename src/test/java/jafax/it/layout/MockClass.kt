package jafax.it.layout

import jafax.collectionEquals
import jafax.mapObjectsToNameList
import jafax.mapMethodsToSignatureList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.vladg.jafax.repository.model.ASTObject
import org.vladg.jafax.repository.model.Class
import org.vladg.jafax.repository.model.Modifier
import kotlin.test.assertEquals

@Serializable
@SerialName("MockClass")
class MockClass(
        var containedFieldNames: Set<String>,
        var containedMethodSignatures: Set<String>,
        var calledMethodSignatures: Set<String> = setOf(),
        var accessedFieldNames: Set<String>,
        var containedClassNames: Set<String>,
        var typeParameterNames: Set<String>,
        var isTypeParameter: Boolean,
        var parameterInstanceNames: Set<String>,
        var superClassName: String? = null,
        var modifiers: Set<String> = HashSet(),
        var fileName: String? = null,
        var isInterface: Boolean,
        var superInterfaceNames: Set<String>,
        var isExternal: Boolean,
        var name: String,
        var containerName: String? = null
): Mock() {

    fun verify(clazz: Class) {
        assertEquals(name, clazz.name)
        collectionEquals(containedFieldNames, mapObjectsToNameList(clazz.containedFields))
        collectionEquals(containedMethodSignatures, mapMethodsToSignatureList(clazz.containedMethods))
        collectionEquals(calledMethodSignatures, mapMethodsToSignatureList(clazz.calledMethods))
        collectionEquals(accessedFieldNames, mapObjectsToNameList(clazz.accessedFields))
        collectionEquals(containedClassNames, mapObjectsToNameList(clazz.containedClasses))
        collectionEquals(typeParameterNames, mapObjectsToNameList(clazz.typeParameters.filterNotNull()))
        assertEquals(isTypeParameter, clazz.isTypeParameter)
        collectionEquals(parameterInstanceNames, mapObjectsToNameList(clazz.parameterInstances.filterNotNull()))
        assertEquals(superClassName, clazz.superClass?.name)
        assertEquals(modifiers.map { Modifier.valueOf(it) }.toSet(), clazz.modifiers)
        assertEquals(isInterface, clazz.isInterface)
        collectionEquals(superInterfaceNames, mapObjectsToNameList(clazz.superInterfaces))
        assertEquals(isExternal, clazz.isExternal)
        assertEquals(containerName, clazz.container?.name)
    }

    override fun isSame(obj: ASTObject) =
        name == obj.name && containerName == obj.container?.name

}