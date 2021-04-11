package org.vladg.ast.service

import com.google.inject.Inject
import org.vladg.ast.repository.NonPersistentRepository
import org.vladg.ast.repository.model.File

class ContextService {

    @Inject
    private lateinit var fileNonPersistentRepository: NonPersistentRepository<File>

}