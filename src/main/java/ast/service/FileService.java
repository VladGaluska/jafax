package ast.service;

import ast.repository.NonPersistentRepository;
import ast.repository.model.File;
import com.google.inject.Inject;

public class FileService {

    @Inject
    private NonPersistentRepository<File> fileRepository;

    public void addFile(File file) {
        fileRepository.addObject(file);
    }

}
