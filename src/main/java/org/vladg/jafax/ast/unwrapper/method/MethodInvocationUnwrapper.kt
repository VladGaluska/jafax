package org.vladg.jafax.ast.unwrapper.method

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.vladg.jafax.ast.unwrapper.ContainerService
import org.vladg.jafax.ast.unwrapper.attribute.AttributeUnwrapper
import org.vladg.jafax.repository.model.container.Method

class MethodInvocationUnwrapper {

    @Inject
    private lateinit var methodUnwrapper: MethodUnwrapper

    @Inject
    private lateinit var attributeUnwrapper: AttributeUnwrapper

    @Inject
    private lateinit var containerService: ContainerService

    fun registerInvocation(methodInvocation: MethodInvocation) {
        setInvocationRelation(findCalledMethod(methodInvocation) ?: return, methodInvocation, methodInvocation.expression)
    }

    fun registerInvocation(classInstanceCreation: ClassInstanceCreation) {
        setInvocationRelation(findCalledMethod(classInstanceCreation) ?: return, classInstanceCreation, null)
    }

    fun registerInvocation(superMethodInvocation: SuperMethodInvocation) {
        setInvocationRelation(findCalledMethod(superMethodInvocation) ?: return, superMethodInvocation, null)
    }

    fun registerInvocation(superConstructorInvocation: SuperConstructorInvocation) {
        setInvocationRelation(findCalledMethod(superConstructorInvocation) ?: return, superConstructorInvocation, null)
    }

    private fun setInvocationRelation(method: Method, node: ASTNode, obj: ASTNode?) =
        containerService.findContainer(node)?.addToInvocations(method, attributeUnwrapper.findAttributeForConnection(obj))

    private fun findCalledMethod(methodInvocation: MethodInvocation) =
        getMethod(methodInvocation.resolveMethodBinding())

    private fun findCalledMethod(superConstructorInvocation: SuperConstructorInvocation) =
        getMethod(superConstructorInvocation.resolveConstructorBinding())

    private fun findCalledMethod(classInstanceCreation: ClassInstanceCreation) =
        getMethod(classInstanceCreation.resolveConstructorBinding())

    private fun findCalledMethod(superMethodInvocation: SuperMethodInvocation) =
        getMethod(superMethodInvocation.resolveMethodBinding())

    private fun getMethod(methodBinding: IMethodBinding?): Method? {
        methodBinding ?: return null
        return methodUnwrapper.findOrCreateMethodForBinding(methodBinding)
                ?.apply { setParameterInstances(methodBinding, this) }
    }

    private fun setParameterInstances(binding: IMethodBinding, originalMethod: Method) {
        if (originalMethod.typeParameters.isNotEmpty()) {
            containerService.setParameterInstances(
                    parameterizedContainer = originalMethod,
                    typeInstances = methodUnwrapper.typeArgumentsForMethodBinding(binding)
            )
        }
    }

}