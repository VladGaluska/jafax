import io.scanner.ProjectScanner;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static Path getPathFromName(String pathName) {
        return Paths.get(pathName).toAbsolutePath().normalize();
    }

    public static void main(String[] args) {
        var path = args.length > 0 ? getPathFromName(args[0]) : getPathFromName(".");
        System.out.println("Scanning for files...");
        ProjectScanner.beginScan(path);
    }

}
