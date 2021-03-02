package jafax.utils;

import org.junit.BeforeClass;
import org.junit.Test;
import utils.filefinder.FileFinder;
import utils.filefinder.JavaFiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FileFinderTest {

    private static JavaFiles files;

    private static final Set<String> EXPECTED_FILE_NAMES = new HashSet<>(Arrays.asList(
            "Empty.java",
            "Client.java",
            "DataObject.java",
            "ExtendedData.java",
            "IProvider.java",
            "Provider.java"
    ));

    @BeforeClass
    public static void initFiles() {
        files = FileFinder.findFiles(Paths.get("src/test/resources/src/org/radum"));
    }

    @Test
    public void shouldFindJavaFiles() {
        List<String> javaFiles = files.javaFiles;
        assertEquals(6, javaFiles.size());
        Set<String> classNames = getSimpleFileNames(javaFiles);
        assertEquals(EXPECTED_FILE_NAMES, classNames);
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
        JavaFiles emptyFiles = FileFinder.findFiles(Paths.get("src/test/resources/src/org/radum/empty/empty2"));
        assertEquals(0, emptyFiles.javaFiles.size());
        assertEquals(0, emptyFiles.jarFiles.size());
    }

}
