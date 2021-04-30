package org.vladg.jafax.ast.service

import com.google.inject.Inject
import org.eclipse.jdt.core.dom.*
import org.vladg.jafax.ast.repository.model.Method

class MethodInvocationService {

    @Inject
    private lateinit var methodService: MethodService

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

    private fun setInvocationRelation(method: Method, node: ASTNode) =
        containerService.findContainer(node)?.addToCalledMethods(method)

    private fun findCalledMethod(methodInvocation: MethodInvocation): Method? =
        getMethod(methodInvocation.resolveMethodBinding())

    private fun findCalledMethod(classInstanceCreation: ClassInstanceCreation): Method? =
        getMethod(classInstanceCreation.resolveConstructorBinding())

    private fun findCalledMethod(superMethodInvocation: SuperMethodInvocation): Method? =
        getMethod(superMethodInvocation.resolveMethodBinding())

    private fun getMethod(methodBinding: IMethodBinding?): Method? =
        methodService.findOrCreateMethodForBinding(methodBinding)

}