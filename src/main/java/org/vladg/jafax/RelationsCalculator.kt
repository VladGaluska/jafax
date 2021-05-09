package org.vladg.jafax

import org.vladg.jafax.repository.ClassRepository

object RelationsCalculator {

    fun calculateRelations() {
        val classes = ClassRepository.getAll()
                            .filter { it.isInternal() }

    }


}