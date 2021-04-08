package org.vladg.ast.service;

import org.vladg.ast.repository.NonPersistentRepository;
import org.vladg.ast.repository.model.File;
import com.google.inject.Inject;

public class ContextService {

    @Inject
    private NonPersistentRepository<File> fileNonPersistentRepository;

}
