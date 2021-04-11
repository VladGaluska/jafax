package org.vladg.io.scanner;

import org.vladg.ast.ASTCreator;
import org.vladg.jafax.utils.filefinder.FileFinder;

import java.nio.file.Path;

public class ProjectScanner {

    public static void beginScan(Path path) {
        var files = FileFinder.findFiles(path);
        var javaFiles = files.javaFiles.toArray(new String[0]);
        var jarFiles = files.jarFiles.toArray(new String[0]);
        ASTCreator.createAst(javaFiles, jarFiles);
    }

}