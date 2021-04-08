package org.vladg.ast.service;

import org.vladg.ast.repository.NonPersistentRepository;
import org.vladg.ast.repository.model.File;
import com.google.inject.Inject;

public class FileService {

    @Inject
    private NonPersistentRepository<File> fileRepository;

    public void addFile(File file) {
        fileRepository.addObject(file);
    }

}
