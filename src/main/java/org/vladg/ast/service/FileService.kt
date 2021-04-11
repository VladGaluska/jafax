package org.vladg.ast.service

import com.google.inject.Inject
import org.vladg.ast.repository.NonPersistentRepository
import org.vladg.ast.repository.model.File

class FileService {

    @Inject
    private lateinit var fileRepository: NonPersistentRepository<File>

    fun addFile(file: File) {
        fileRepository.addObject(file)
    }

}