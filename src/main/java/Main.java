import io.scanner.ProjectScanner;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Main {

    private static Path getPathFromName(String pathName) {
        return Paths.get(pathName).toAbsolutePath().normalize();
    }

    public static void main(String[] args) {
        var path = args.length > 0 ? getPathFromName(args[0]) : getPathFromName(".");
        log.info("Scanning for files...");
        ProjectScanner.beginScan(path);
    }

}
