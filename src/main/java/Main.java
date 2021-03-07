import ast.ASTCreator;
import ast.ASTVisitor;
import utils.filefinder.FileFinder;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static Path getPathFromName(String pathName) {
        return Paths.get(pathName).toAbsolutePath().normalize();
    }

    public static void main(String[] args) {
        var path = args.length > 0 ? getPathFromName(args[0]) : getPathFromName(".");
        System.out.println("Scanning for files...");
        var files = FileFinder.findFiles(path);
        var javaFiles = files.javaFiles.toArray(new String[0]);
        var jarFiles = files.jarFiles.toArray(new String[0]);
        ASTCreator.createAst(javaFiles, jarFiles);
        ASTVisitor.getInstance().getClassNames().forEach(System.out::println);
    }

}
