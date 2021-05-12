package org.vladg.jafax.ast.unwrapper

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.vladg.jafax.repository.model.Method

class MethodInvocationUnwrapper {

    @Inject
    private lateinit var methodUnwrapper: MethodUnwrapper

    @Inject
    private lateinit var containerService: ContainerService

    fun registerInvocation(methodInvocation: MethodInvocation) {
        setInvocationRelation(findCalledMethod(methodInvocation) ?: return, methodInvocation)
    }

    fun registerInvocation(classInstanceCreation: ClassInstanceCreation) {
        setInvocationRelation(findCalledMethod(classInstanceCreation) ?: return, classInstanceCreation)
    }

    fun registerInvocation(superMethodInvocation: SuperMethodInvocation) {
        setInvocationRelation(findCalledMethod(superMethodInvocation) ?: return, superMethodInvocation)
    }

    fun registerInvocation(superConstructorInvocation: SuperConstructorInvocation) {
        setInvocationRelation(findCalledMethod(superConstructorInvocation) ?: return, superConstructorInvocation)
    }

    private fun setInvocationRelation(method: Method, node: ASTNode) =
        containerService.findContainer(node)?.addToCalledMethods(method)

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