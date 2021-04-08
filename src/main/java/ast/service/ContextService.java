package ast.service;

import ast.repository.NonPersistentRepository;
import ast.repository.model.File;
import com.google.inject.Inject;

public class ContextService {

    @Inject
    private NonPersistentRepository<File> fileNonPersistentRepository;

}
