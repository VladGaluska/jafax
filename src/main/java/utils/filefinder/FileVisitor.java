package utils.filefinder;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class FileVisitor extends SimpleFileVisitor<Path> {

    private final PathMatcher javaMatcher;

    private final PathMatcher jarMatcher;

    @Getter
    private final List<String> javaFiles;

    @Getter
    private final List<String> jarFiles;

    public FileVisitor() {
        this.javaFiles = new ArrayList<>();
        this.jarFiles = new ArrayList<>();
        this.javaMatcher = FileSystems.getDefault()
                                      .getPathMatcher("glob:*.java");
        this.jarMatcher = FileSystems.getDefault()
                                     .getPathMatcher("glob:*.jar");
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        var name = file.getFileName();
        if (name != null && attrs.isRegularFile()) {
            if (javaMatcher.matches(name)) {
                javaFiles.add(file.toString());
            } else if (jarMatcher.matches(name)) {
                jarFiles.add(file.toString());
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        exc.printStackTrace();
        return FileVisitResult.CONTINUE;
    }

}
