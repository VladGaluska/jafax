package jafax.utils;

import org.junit.Test;
import utils.filefinder.FileFinder;
import utils.filefinder.JavaFiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FileFinderTest {

    private static final Set<String> EXPECTED_FILE_NAMES = new HashSet<>(Arrays.asList(
            "Empty.java",
            "Client.java",
            "DataObject.java",
            "ExtendedData.java",
            "IProvider.java",
            "Provider.java"
    ));

    private static final Set<String> EXPECTED_SINGLE_FILE_NAME = new HashSet<>(Collections.singletonList(
            "Empty.java"
    ));

    @Test
    public void shouldFindJavaFiles() {
        JavaFiles files = FileFinder.findFiles(Paths.get("src/test/resources/org/radum"));
        List<String> javaFiles = files.javaFiles;
        assertEquals(6, javaFiles.size());
        assertEquals(0, files.jarFiles.size());
        Set<String> classNames = getSimpleFileNames(javaFiles);
        assertEquals(EXPECTED_FILE_NAMES, classNames);
    }

    @Test
    public void shouldFindSingleJavaFile() {
        JavaFiles files = FileFinder.findFiles(Paths.get("src/test/resources/org/radum/empty/empty1"));
        List<String> javaFiles = files.javaFiles;
        assertEquals(1, javaFiles.size());
        assertEquals(0, files.jarFiles.size());
        Set<String> classNames = getSimpleFileNames(javaFiles);
        assertEquals(EXPECTED_SINGLE_FILE_NAME, classNames);
    }

    private Set<String> getSimpleFileNames(List<String> fileNames) {
        return fileNames.stream()
                        .map(Paths::get)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .collect(Collectors.toSet());
    }

    @Test
    public void shouldNotFindAnything() {
        JavaFiles emptyFiles = FileFinder.findFiles(Paths.get("src/test/resources/org/radum/empty/empty2"));
        assertEquals(0, emptyFiles.javaFiles.size());
        assertEquals(0, emptyFiles.jarFiles.size());
    }

}
