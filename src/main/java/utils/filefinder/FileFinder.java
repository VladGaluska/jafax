package utils.filefinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileFinder {

    public static JavaFiles findFiles(Path path) {
        try {
            FileVisitor fileVisitor = new FileVisitor();
            Files.walkFileTree(path, fileVisitor);
            return JavaFiles.builder()
                            .jarFiles(fileVisitor.getJarFiles())
                            .javaFiles(fileVisitor.getJavaFiles())
                            .build();
        } catch (IOException ex) {
            System.out.println("There is an issue with the path: " + path.toString());
        }
        return new JavaFiles();
    }

}
