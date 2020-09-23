import AST.ASTCreator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static Path getPathFromName(String pathName) {
        return Paths.get(pathName).toAbsolutePath().normalize();
    }

    public static void main(String[] args) {
        Path path = args.length > 0 ? getPathFromName(args[0]) : getPathFromName(".");
        System.out.println("Scanning for files...");
        String[] javaFiles = FileFinder.findFiles(".java", path.toString()).toArray(new String[0]);
        String[] jarFiles = FileFinder.findFiles(".jar", path.toString()).toArray(new String[0]);
        ASTCreator.createAst(javaFiles, jarFiles);
    }

}
