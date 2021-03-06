package org.dxworks.dxplatform.plugins.insider.library.detector;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JavaImportsContainer extends ImportsContainer {
    private static final String IGNORED_IMPORTS_FILE_NAME = "config" + File.separator + "ignoredImports.txt";

    private static final String IMPORTS_OUTPUT_FILE_NAME = "Imports.csv";
    private static final String FILES_WITH_IMPORTS_NAME = "FilesWithImports.csv";
    private static final String PACKAGE_IMPORTS_OUTPUT_FILE_NAME = "PackageImports.csv";

    private List<String> ignoredImports;

    public void configure() {
        try {
            ignoredImports = Files.readAllLines(Paths.get(IGNORED_IMPORTS_FILE_NAME));
        } catch (IOException e) {
            log.error("Could not read ignored imports file!");
            ignoredImports = new ArrayList<>();
        }
    }

    @Override
    protected String getImportsResultFilePath() {
        return IMPORTS_OUTPUT_FILE_NAME;
    }

    @Override
    protected String getFilesWithImportsResultFile() {
        return FILES_WITH_IMPORTS_NAME;
    }

    @Override
    protected String getPackagingUnitResultFilePath() {
        return PACKAGE_IMPORTS_OUTPUT_FILE_NAME;
    }

    @Override
    public boolean accepts(String importString) {
        return ignoredImports.stream().noneMatch(importString::startsWith);
    }

    @Override
    protected String getPackagingUnitFor(String importedFile) {
        return getPackageName(importedFile);
    }

    private String getPackageName(String _import) {
        int index = _import.lastIndexOf('.');
        if (index == -1)
            return _import;

        return _import.substring(0, index);
    }
}
