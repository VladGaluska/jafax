package org.dxworks.dxplatform.plugins.insider.library.detector;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class C_ImportsContainer extends ImportsContainer {

    private static final String HEADERS_TO_LIBRARIES_MAPPING_FILE_NAME = "config" + File.separator + "headersToLibraries.csv";

    private static final String HEADERS_IMPORTED_OUTPUT_FILE_NAME = "Headers.csv";
    private static final String FILES_WHIT_HEADERS_OUTPUT_FILE_NAME = "FilesWithHeaders.csv";
    private static final String LIBRARIES_OUTPUT_FILE_NAME = "Libraries.csv";

    private HashMap<String, String> headersToLibraries = new HashMap<>();


    protected void configure() {

        try {
            List<String> stringList = Files.readAllLines(Paths.get(HEADERS_TO_LIBRARIES_MAPPING_FILE_NAME));
            stringList.forEach(line ->
            {
                String[] pair = line.split("\t");
                if (pair.length != 2) {
                    log.error("Incorrect input type for 'headersToLibraries.csv'. Headers and libraries have to be tab separated!");
                    return;
                }
                String header = pair[0];
                String library = pair[1];

                if (headersToLibraries.containsKey(header)) {
                    System.out.println("Header " + header + " present in 2 libraries: " + headersToLibraries.get(header) + " and " + library + "!");
                    return;
                }

                headersToLibraries.put(header, library);
            });
        } catch (IOException e) {
            log.error("Could not read headersToLibraries.csv file!");
        }
    }

    @Override
    protected String getImportsResultFilePath() {
        return HEADERS_IMPORTED_OUTPUT_FILE_NAME;
    }

    @Override
    protected String getFilesWithImportsResultFile() {
        return FILES_WHIT_HEADERS_OUTPUT_FILE_NAME;
    }

    @Override
    protected String getPackagingUnitResultFilePath() {
        return LIBRARIES_OUTPUT_FILE_NAME;
    }

    @Override
    public boolean accepts(String importString) {
        return false;
    }

    @Override
    protected String getPackagingUnitFor(String importedFile) {
        return headersToLibraries.get(importedFile);
    }
}
