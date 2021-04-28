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
        this.setInvocationRelation(this.findCalledMethod(methodInvocation) ?: return, methodInvocation)
    }

    fun registerInvocation(classInstanceCreation: ClassInstanceCreation) {
        this.setInvocationRelation(this.findCalledMethod(classInstanceCreation) ?: return, classInstanceCreation)
    }

    fun registerInvocation(superMethodInvocation: SuperMethodInvocation) {
        this.setInvocationRelation(this.findCalledMethod(superMethodInvocation) ?: return, superMethodInvocation)
    }

    private fun setInvocationRelation(method: Method, node: ASTNode) =
        this.containerService.findContainer(node)?.addToCalledMethods(method)

    private fun findCalledMethod(methodInvocation: MethodInvocation): Method? =
        this.getMethod(methodInvocation.resolveMethodBinding())

    private fun findCalledMethod(classInstanceCreation: ClassInstanceCreation): Method? =
        this.getMethod(classInstanceCreation.resolveConstructorBinding())

    private fun findCalledMethod(superMethodInvocation: SuperMethodInvocation): Method? =
        this.getMethod(superMethodInvocation.resolveMethodBinding())

    private fun getMethod(methodBinding: IMethodBinding?): Method? =
        this.methodService.findOrCreateMethodForBinding(methodBinding)

}