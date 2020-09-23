import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {

    public static List<String> findFiles(String extension, File root) {
        List<String> files = new ArrayList<>();
        for (File child : root.listFiles()) {
            if (child.isDirectory()) {
                files.addAll(findFiles(extension, child));
            } else {
                if (child.getName().endsWith(extension)) {
                    files.add(child.getAbsolutePath());
                }
            }
        }
        return files;
    }

    public static List<String> findFiles(String extension, String rootPath) {
        return findFiles(extension, new File(rootPath));
    }

}
