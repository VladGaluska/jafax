package jafax.it

import jafax.getSimpleProjectPath
import org.junit.Test
import org.vladg.jafax.io.scanner.ProjectScanner
import org.vladg.jafax.repository.ClassRepository
import org.vladg.jafax.repository.CommonRepository
import org.vladg.jafax.repository.model.*
import org.vladg.jafax.utils.extensions.getLayoutFile
import org.vladg.jafax.repository.model.Attribute.AttributeKind
import kotlin.test.*

class LayoutIT {//This is really bad but I did this when I was very tired and by the time I figures a better way to do it, I already did 1/4th :(

    private val projectPath = getSimpleProjectPath()

    @BeforeTest
    fun clearRepositories() {
        ClassRepository.clear()
        CommonRepository.clear()
    }

    @Test
    fun `should properly extract data without file`() {
        val layoutFile = getLayoutFile(projectPath)
        layoutFile.delete()
        scanAndVerify()
    }

    @Test
    fun `should properly extract data with file`() {
        scanAndVerify()
    }

    private fun scanAndVerify() {
        ProjectScanner.beginScan(projectPath)
        verifyLayout()
    }

    private fun verifyLayout() {
        `should contain right classes`()
        `should contain right methods`()
        `should contain right attributes`()
    }

    private fun `should contain right classes`() {
        val classes = ClassRepository.getAll()
        assertEquals(9, classes.filter { !it.isExternal }.size)
        `classes should be correct`(classes)
    }

    private fun `classes should be correct`(classes: Collection<Class>) {
        classes.forEach {
            when(it.name) {
                "Client" -> verifyClient(it)
                "DataObject" -> verifyDataObject(it)
                "ExtendedData" -> verifyExtendedData(it)
                "Provider" -> verifyProvider(it)
                "Empty" -> verifyEmpty(it)
                "SubEmpty" -> verifySubEmpty(it)
                "IProvider" -> verifyIProvider(it)
                "ProviderFactory"-> verifyProviderFactory(it)
                "" -> verifyAnonymousClass(it)
                else -> assertTrue(it.isExternal, "Class ${it.name} should be set as external!")
            }
        }
    }

    private fun verifyClient(client: Class) {
        `class should have the proper amount of attributes`(client, 3)
        `objects should match names`(client.containedFields,
                setOf(
                        "dataMember",
                        "providers",
                        "extendedData"
                )
        )
        `objects should match names`(client.containedMethods,
                setOf(
                        "useObject",
                        "anotherFunction",
                        "client",
                        "main"
                )
        )
        `objects should match names`(client.calledMethods,
                setOf(
                        "getProvider",
                        "ExtendedData"
                )
        )
        assertTrue(client.accessedFields.isEmpty())
        assertTrue(client.containedClasses.isEmpty())
        assertEquals("Object", client.superClass?.name)
        assertEquals(setOf(Modifier.Public), client.modifiers)
        assertEquals(client.fileName, "Client.java")
        assertFalse(client.isInterface)
        assertTrue(client.superInterfaces.isEmpty())
        assertFalse(client.isExternal)
        assertNull(client.container)
    }

    private fun verifyDataObject(dataObject: Class) {
        `class should have the proper amount of attributes`(dataObject, 2)
        `objects should match names`(dataObject.containedFields,
                setOf(
                        "x",
                        "y"
                )
        )
        assertTrue(dataObject.containedClasses.isEmpty())
        `objects should match names`(dataObject.containedMethods,
                setOf(
                        "DataObject"
                )
        )
        assertTrue(dataObject.calledMethods.isEmpty())
        assertTrue(dataObject.accessedFields.isEmpty())
        assertEquals("Object", dataObject.superClass?.name)
        assertEquals(setOf(Modifier.Public), dataObject.modifiers)
        assertEquals(dataObject.fileName, "DataObject.java")
        assertFalse(dataObject.isInterface)
        assertTrue(dataObject.superInterfaces.isEmpty())
        assertFalse(dataObject.isExternal)
        assertNull(dataObject.container)
    }

    private fun verifyExtendedData(extendedData: Class) {
        `class should have the proper amount of attributes`(extendedData, 1)
        `objects should match names`(extendedData.containedFields,
                setOf(
                        "providerObject"
                )
        )
        `objects should match names`(extendedData.containedMethods,
                setOf(
                        "ExtendedData",
                        "setProvider"
                )
        )
        assertTrue(extendedData.containedClasses.isEmpty())
        assertTrue(extendedData.calledMethods.isEmpty())
        assertTrue(extendedData.accessedFields.isEmpty())
        assertTrue(extendedData.superClass?.name == "DataObject")
        assertEquals(setOf(Modifier.Public), extendedData.modifiers)
        assertEquals(extendedData.fileName, "ExtendedData.java")
        assertFalse(extendedData.isInterface)
        assertTrue(extendedData.superInterfaces.isEmpty())
        assertFalse(extendedData.isExternal)
        assertNull(extendedData.container)
    }

    private fun verifyProvider(provider: Class) {
        `class should have the proper amount of attributes`(provider, 3)
        `objects should match names`(provider.containedFields,
                setOf(
                        "data",
                        "extendedData",
                        "anonymousProvider"
                )
        )
        `objects should match names`(provider.containedMethods,
                setOf(
                        "Provider",
                        "measureComplexity",
                        "anotherFunction",
                        "getData"
                )
        )
        `objects should match names`(provider.containedClasses,
                setOf(
                        ""
                )
        )
        `objects should match names`(provider.calledMethods,
                setOf(
                        ""
                )
        )
        assertTrue(provider.accessedFields.isEmpty())
        assertEquals("Object", provider.superClass?.name)
        assertEquals(setOf(Modifier.Public), provider.modifiers)
        assertEquals(provider.fileName, "Provider.java")
        assertFalse(provider.isInterface)
        assertTrue(provider.superInterfaces.size == 1)
        assertTrue(provider.superInterfaces.firstOrNull()?.name == "IProvider")
        assertFalse(provider.isExternal)
        assertNull(provider.container)
    }

    private fun verifyEmpty(empty: Class) {
        `class should have the proper amount of attributes`(empty, 0)
        `objects should match names`(empty.containedClasses,
                setOf(
                        "SubEmpty"
                )
        )
        assertTrue(empty.calledMethods.isEmpty())
        assertTrue(empty.containedMethods.isEmpty())
        assertTrue(empty.accessedFields.isEmpty())
        assertEquals("Object", empty.superClass?.name)
        assertTrue(empty.modifiers.isEmpty())
        assertEquals(empty.fileName, "Client.java")
        assertFalse(empty.isInterface)
        assertTrue(empty.superInterfaces.isEmpty())
        assertFalse(empty.isExternal)
        assertNull(empty.container)
    }

    private fun verifySubEmpty(subEmpty: Class) {
        `class should have the proper amount of attributes`(subEmpty, 0)
        assertTrue(subEmpty.containedClasses.isEmpty())
        assertTrue(subEmpty.calledMethods.isEmpty())
        assertTrue(subEmpty.containedMethods.isEmpty())
        assertTrue(subEmpty.accessedFields.isEmpty())
        assertEquals("Object", subEmpty.superClass?.name)
        assertTrue(subEmpty.modifiers.isEmpty())
        assertNull(subEmpty.fileName)
        assertFalse(subEmpty.isInterface)
        assertTrue(subEmpty.superInterfaces.isEmpty())
        assertFalse(subEmpty.isExternal)
        assertEquals("Empty", subEmpty.container?.name)
    }

    private fun verifyIProvider(iProvider: Class) {
        `class should have the proper amount of attributes`(iProvider, 0)
        `objects should match names`(iProvider.containedMethods,
                setOf(
                        "anotherFunction"
                )
        )
        assertTrue(iProvider.containedClasses.isEmpty())
        assertTrue(iProvider.calledMethods.isEmpty())
        assertTrue(iProvider.accessedFields.isEmpty())
        assertNull(iProvider.superClass)
        assertTrue(iProvider.modifiers.isEmpty())
        assertEquals(iProvider.fileName, "IProvider.java")
        assertTrue(iProvider.isInterface)
        assertTrue(iProvider.superInterfaces.isEmpty())
        assertFalse(iProvider.isExternal)
        assertNull(iProvider.container)
    }

    private fun verifyProviderFactory(providerFactory: Class) {
        `class should have the proper amount of attributes`(providerFactory, 0)
        assertTrue(providerFactory.containedClasses.isEmpty())
        assertTrue(providerFactory.calledMethods.isEmpty())
        assertTrue(providerFactory.accessedFields.isEmpty())
        assertEquals("Object", providerFactory.superClass?.name)
        assertTrue(providerFactory.modifiers.isEmpty())
        assertEquals(providerFactory.fileName, "Provider.java")
        assertFalse(providerFactory.isInterface)
        assertTrue(providerFactory.superInterfaces.isEmpty())
        assertFalse(providerFactory.isExternal)
        assertNull(providerFactory.container)
    }

    private fun verifyAnonymousClass(it: Class) {
        `class should have the proper amount of attributes`(it, 0)
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.calledMethods.isEmpty())
        assertTrue(it.accessedFields.isEmpty())
        assertEquals("Object", it.superClass?.name)
        assertTrue(it.modifiers.isEmpty())
        assertNull(it.fileName)
        assertFalse(it.isInterface)
        `objects should match names`(it.superInterfaces,
                setOf(
                        "IProvider"
                )
        )
        assertFalse(it.isExternal)
        assertEquals("Provider", it.container?.name)
    }

    private fun `objects should match names`(objects: Collection<ASTObject>, names: Collection<String>) {
        val objectNames = objects.map { it.name }
        names.forEach {
            assertTrue(objectNames.contains(it), "Object with name: $it not found!")
        }
    }

    private fun `class should have the proper amount of attributes`(clazz: Class, amount: Int) =
        assertEquals(amount, clazz.containedFields.size, "Attribute amount for class ${clazz.name} is wrong!")

    private fun `should contain right methods`() {
        val methods = CommonRepository.findByFilter {
            it is Method && it.isInternal()
        }.map { it as Method }
        assertEquals(16, methods.size)
        `methods should be correct`(methods)
    }

    private fun `methods should be correct`(methods: Collection<Method>) {
        methods.forEach {
            when(it.signature) {
                "useObject(org.radum.Provider)" -> checkUseObject(it)
                "anotherFunction(org.radum.ExtendedData)" -> checkAnotherFunction(it)
                "client()" -> checkClient(it)
                "main(java.lang.String[])" -> checkMain(it)
                "ExtendedData()" -> checkExtendedData(it)
                "ExtendedData(int)" -> checkExtendedDataWithInt(it)
                "setProvider(org.radum.Provider)" -> checkSetProvider(it)
                "anotherFunction()" -> checkAnotherFunctionNoArgs(it)
                "Provider(int)" -> checkProvider(it)
                "measureComplexity(int, int)" -> checkMeasureComplexity(it)
                "getData()" -> checkGetData(it)
                "getProvider()" -> checkGetProvider(it)
                "DataObject()" -> checkDataObject(it)
                "()" -> checkAnonymousMethod(it)
                else -> assertTrue(false, "Method with signature ${it.signature} should be external or handled!")
            }
        }
    }

    private fun checkDataObject(it: Method){
        assertTrue(it.isConstructor)
        assertTrue(it.calledMethods.isEmpty())
        assertTrue(it.parameters.isEmpty())
        assertTrue(it.localVariables.isEmpty())
        assertTrue(it.accessedFields.isEmpty())
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("DataObject", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun checkUseObject(it: Method) {
        assertFalse(it.isConstructor)
        `objects should match names`(it.calledMethods,
                setOf(
                        "println",
                        "measureComplexity",
                        "getData"
                )
        )
        `objects should match names`(it.parameters,
                setOf(
                        "dprovider"
                )
        )
        assertTrue(it.localVariables.isEmpty())
        `objects should match names`(it.accessedFields,
                setOf(
                        "x",
                        "y",
                        "err"
                )
        )
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("Client", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun checkAnotherFunction(it: Method) {
        assertFalse(it.isConstructor)
        `objects should match names`(it.calledMethods,
                setOf(
                        "measureComplexity",
                        "add",
                        "ArrayList<String>",
                        "stream",
                        "forEach"
                )
        )
        `objects should match names`(it.parameters,
                setOf(
                        "extendedData"
                )
        )
        `objects should match names`(it.localVariables,
                setOf(
                        "values"
                )
        )
        `objects should match names`(it.accessedFields,
                setOf(
                        "x"
                )
        )
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("Client", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun checkClient(it: Method) {
        assertFalse(it.isConstructor)
        `objects should match names`(it.calledMethods,
                setOf(
                        "anotherFunction",
                        "println"
                )
        )
        assertTrue(it.parameters.isEmpty())
        `objects should match names`(it.localVariables,
                setOf(
                        "unused",
                        "result"
                )
        )
        `objects should match names`(it.accessedFields,
                setOf(
                        "err"
                )
        )
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(3, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("Client", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun checkMain(it: Method) {
        assertFalse(it.isConstructor)
        assertTrue(it.calledMethods.isEmpty())
        `objects should match names`(it.parameters,
                setOf(
                        "args"
                )
        )
        assertTrue(it.localVariables.isEmpty())
        assertTrue(it.accessedFields.isEmpty())
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public, Modifier.Static), it.modifiers)
        assertEquals("Client", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun checkExtendedData(it: Method) {
        assertTrue(it.isConstructor)
        `objects should match names`(it.calledMethods,
                setOf(
                        "Provider"
                )
        )
        assertTrue(it.parameters.isEmpty())
        assertTrue(it.localVariables.isEmpty())
        assertTrue(it.accessedFields.isEmpty())
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("ExtendedData", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun checkSetProvider(it: Method) {
        assertFalse(it.isConstructor)
        assertTrue(it.calledMethods.isEmpty())
        `objects should match names`(it.parameters,
                setOf(
                        "_p"
                )
        )
        assertTrue(it.localVariables.isEmpty())
        assertTrue(it.accessedFields.isEmpty())
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("ExtendedData", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun checkAnotherFunctionNoArgs(it: Method) {
        when (it.container?.name) {
            "IProvider" -> {
                assertFalse(it.isConstructor)
                assertTrue(it.calledMethods.isEmpty())
                assertTrue(it.parameters.isEmpty())
                assertTrue(it.localVariables.isEmpty())
                assertTrue(it.accessedFields.isEmpty())
                assertTrue(it.containedClasses.isEmpty())
                assertTrue(it.containedMethods.isEmpty())
                assertEquals(1, it.cyclomaticComplexity)
                assertEquals(setOf(Modifier.Public, Modifier.Abstract), it.modifiers)
                assertEquals("int", it.returnType?.name)
            }
            "Provider" -> {
                assertFalse(it.isConstructor)
                assertTrue(it.calledMethods.isEmpty())
                assertTrue(it.parameters.isEmpty())
                `objects should match names`(it.localVariables,
                        setOf(
                                "localData"
                        )
                )
                `objects should match names`(it.accessedFields,
                        setOf(
                                "x",
                                "y"
                        )
                )
                assertTrue(it.containedClasses.isEmpty())
                assertTrue(it.containedMethods.isEmpty())
                assertEquals(1, it.cyclomaticComplexity)
                assertEquals(setOf(Modifier.Public), it.modifiers)
                assertEquals("int", it.returnType?.name)
            }
            else -> {
                assertFalse(it.isConstructor)
                assertTrue(it.calledMethods.isEmpty())
                assertTrue(it.parameters.isEmpty())
                assertTrue(it.localVariables.isEmpty())
                `objects should match names`(it.accessedFields,
                        setOf(
                                "x",
                                "y"
                        )
                )
                assertTrue(it.containedClasses.isEmpty())
                assertTrue(it.containedMethods.isEmpty())
                assertEquals(1, it.cyclomaticComplexity)
                assertEquals(setOf(Modifier.Public), it.modifiers)
                assertEquals("int", it.returnType?.name)
                assertEquals("", it.container?.name)
            }
        }
    }

    private fun checkProvider(it: Method) {
        assertTrue(it.isConstructor)
        assertTrue(it.calledMethods.isEmpty())
        `objects should match names`(it.parameters,
                setOf(
                        "a"
                )
        )
        `objects should match names`(it.localVariables,
                setOf(
                        "x"
                )
        )
        assertTrue(it.accessedFields.isEmpty())
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("Provider", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun checkMeasureComplexity(it: Method) {
        assertFalse(it.isConstructor)
        assertTrue(it.calledMethods.isEmpty())
        `objects should match names`(it.parameters,
                setOf(
                        "i",
                        "j"
                )
        )
        `objects should match names`(it.localVariables,
                setOf(
                        "result"
                )
        )
        assertTrue(it.accessedFields.isEmpty())
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(3, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("Provider", it.container?.name)
        assertEquals("int", it.returnType?.name)
    }

    private fun checkGetData(it: Method) {
        assertFalse(it.isConstructor)
        assertTrue(it.calledMethods.isEmpty())
        assertTrue(it.parameters.isEmpty())
        assertTrue(it.localVariables.isEmpty())
        assertTrue(it.accessedFields.isEmpty())
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("Provider", it.container?.name)
        assertEquals("DataObject", it.returnType?.name)
    }

    private fun checkGetProvider(it: Method) {
        assertFalse(it.isConstructor)
        `objects should match names`(it.calledMethods,
                setOf(
                        "Provider"
                )
        )
        assertTrue(it.parameters.isEmpty())
        assertTrue(it.localVariables.isEmpty())
        assertTrue(it.accessedFields.isEmpty())
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public, Modifier.Static), it.modifiers)
        assertEquals("ProviderFactory", it.container?.name)
        assertEquals("Provider", it.returnType?.name)
    }

    private fun checkExtendedDataWithInt(it: Method) {
        assertTrue(it.isConstructor)
        `objects should match names`(it.calledMethods,
                setOf(
                        "DataObject"
                )
        )
        `objects should match names`(it.parameters,
                setOf(
                        "x"
                )
        )
        assertTrue(it.localVariables.isEmpty())
        `objects should match names`(it.accessedFields,
                setOf(
                        "x"
                )
        )
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("ExtendedData", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun checkAnonymousMethod(it: Method) {
        assertTrue(it.isConstructor)
        assertTrue(it.calledMethods.isEmpty())
        assertTrue(it.parameters.isEmpty())
        assertTrue(it.localVariables.isEmpty())
        assertTrue(it.accessedFields.isEmpty())
        assertTrue(it.containedClasses.isEmpty())
        assertTrue(it.containedMethods.isEmpty())
        assertEquals(1, it.cyclomaticComplexity)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("", it.container?.name)
        assertEquals("void", it.returnType?.name)
    }

    private fun `should contain right attributes`() {
        val attributes = CommonRepository.findByFilter {
            it is Attribute && it.isInternal()
        }.map {
            it as Attribute
        }
        assertEquals(23, attributes.size)
        `attributes should be correct`(attributes)
    }

    private fun `attributes should be correct`(attributes: Collection<Attribute>) {
        attributes.forEach {
            when(it.name) {
                "dataMember", "providers" -> checkDataMemberOrProviders(it)
                "extendedData" -> checkExtendedData(it)
                "dprovider" -> checkDProvider(it)
                "unused" -> checkUnused(it)
                "result" -> checkResult(it)
                "args" -> checkArgs(it)
                "x" -> checkX(it)
                "y" -> checkY(it)
                "providerObject" -> checkProviderObject(it)
                "_p" -> check_p(it)
                "a" -> checkA(it)
                "i", "j" -> checkIOrJ(it)
                "localData" -> checkLocalData(it)
                "data" -> checkData(it)
                "anonymousProvider" -> checkAnonymousProvider(it)
                "values" -> checkValues(it)
                "z" -> checkZ(it)
                else -> assertFalse(true, "Attribute ${it.name} was either missed or should be external")
            }
        }
    }

    private fun checkDataMemberOrProviders(it: Attribute) {
        assertEquals(AttributeKind.Field, it.kind)
        assertEquals("Provider", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("Client", it.container?.name)
    }

    private fun checkExtendedData(it: Attribute) {
        when (it.container?.name) {
            "Client" -> {
                assertEquals(AttributeKind.Field, it.kind)
                assertEquals("ExtendedData", it.type?.name)
                assertTrue(it.modifiers.isEmpty())
            }
            "anotherFunction" -> {
                assertEquals(AttributeKind.Parameter, it.kind)
                assertEquals("ExtendedData", it.type?.name)
                assertEquals(setOf(Modifier.Final), it.modifiers)
            }
            else -> {
                assertEquals(AttributeKind.Field, it.kind)
                assertEquals("ExtendedData", it.type?.name)
                assertEquals(setOf(Modifier.Public), it.modifiers)
                assertEquals("Provider", it.container?.name)
            }
        }
    }

    private fun checkDProvider(it: Attribute) {
        assertEquals(AttributeKind.Parameter, it.kind)
        assertEquals("Provider", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("useObject", it.container?.name)
    }

    private fun checkUnused(it: Attribute) {
        assertEquals(AttributeKind.LocalVariable, it.kind)
        assertEquals("int", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("client", it.container?.name)
    }

    private fun checkResult(it: Attribute) {
        if (it.container?.name == "client") {
            assertEquals(AttributeKind.LocalVariable, it.kind)
            assertEquals("int", it.type?.name)
            assertTrue(it.modifiers.isEmpty())
        } else {
            assertEquals(AttributeKind.LocalVariable, it.kind)
            assertEquals("int", it.type?.name)
            assertTrue(it.modifiers.isEmpty())
            assertEquals("measureComplexity", it.container?.name)
        }
    }

    private fun checkArgs(it: Attribute) {
        assertEquals(AttributeKind.Parameter, it.kind)
        assertEquals("String", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("main", it.container?.name)
    }

    private fun checkX(it: Attribute) {
        assertEquals("int", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        when (it.container?.name) {
            "DataObject" -> assertEquals(AttributeKind.Field, it.kind)
            "Provider" -> assertEquals(AttributeKind.LocalVariable, it.kind)
            else -> {
                assertEquals(AttributeKind.Parameter, it.kind)
                assertEquals("ExtendedData", it.container?.name)
            }
        }
    }

    private fun checkY(it: Attribute) {
        assertEquals(AttributeKind.Field, it.kind)
        assertEquals("int", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("DataObject", it.container?.name)
    }

    private fun checkProviderObject(it: Attribute) {
        assertEquals(AttributeKind.Field, it.kind)
        assertEquals("Provider", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("ExtendedData", it.container?.name)
    }

    private fun check_p(it: Attribute) {
        assertEquals(AttributeKind.Parameter, it.kind)
        assertEquals("Provider", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("setProvider", it.container?.name)
    }

    private fun checkA(it: Attribute) {
        assertEquals(AttributeKind.Parameter, it.kind)
        assertEquals("int", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("Provider", it.container?.name)
    }

    private fun checkIOrJ(it: Attribute) {
        assertEquals(AttributeKind.Parameter, it.kind)
        assertEquals("int", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("measureComplexity", it.container?.name)
    }

    private fun checkLocalData(it: Attribute) {
        assertEquals(AttributeKind.LocalVariable, it.kind)
        assertEquals("DataObject", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("anotherFunction", it.container?.name)
    }

    private fun checkData(it: Attribute) {
        assertEquals(AttributeKind.Field, it.kind)
        assertEquals("DataObject", it.type?.name)
        assertEquals(setOf(Modifier.Public), it.modifiers)
        assertEquals("Provider", it.container?.name)
    }

    private fun checkAnonymousProvider(it: Attribute) {
        assertEquals(AttributeKind.Field, it.kind)
        assertEquals("IProvider", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("Provider", it.container?.name)
    }

    private fun checkZ(it: Attribute) {
        assertEquals(AttributeKind.Parameter, it.kind)
        assertEquals("String", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("lambda", it.container?.name)
    }

    private fun checkValues(it: Attribute) {
        assertEquals(AttributeKind.LocalVariable, it.kind)
        assertEquals("List<String>", it.type?.name)
        assertTrue(it.modifiers.isEmpty())
        assertEquals("anotherFunction", it.container?.name)
    }

}