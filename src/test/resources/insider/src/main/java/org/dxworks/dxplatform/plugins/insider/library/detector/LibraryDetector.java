package org.dxworks.dxplatform.plugins.insider.library.detector;

import lombok.extern.slf4j.Slf4j;
import org.dxworks.dxplatform.plugins.insider.InsiderAnalysis;
import org.dxworks.dxplatform.plugins.insider.InsiderFile;
import org.dxworks.dxplatform.plugins.insider.InsiderResult;
import org.dxworks.dxplatform.plugins.insider.technology.finder.LanguageRegistry;
import org.dxworks.dxplatform.plugins.insider.utils.FileUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LibraryDetector implements InsiderAnalysis {

    private static final String JAVA_LANGUAGE = "java";
    private static final List<String> C_LIKE_LANGUAGES = Arrays.asList("c", "c++", "oc");

    private LanguageRegistry languageRegistry = LanguageRegistry.getInstance();
    private ImportsContainer importsContainer;
    private LibraryDetectorLanguage language;

    public LibraryDetector(LibraryDetectorLanguage language) {
        this.language = language;
        if (language == LibraryDetectorLanguage.C_LIKE)
            importsContainer = new C_ImportsContainer();
        if (language == LibraryDetectorLanguage.JAVA)
            importsContainer = new JavaImportsContainer();
    }

    public InsiderResult analyze(InsiderFile file) {
        int importNumber = 0;
        String content = file.getContent();

        content = FileUtils.removeComments(content);

        if (file.getExtension().equals("java")) {
            importNumber = analyzeForJava(content, file.getPath());
        } else if (file.getExtension().equals("m") || file.getExtension().equals("mm") || file.getExtension()
                .equals("h") || file.getExtension().equals("cpp") || file.getExtension().equals("c")) {
            importNumber = analyzeForC_Like(content);
        }

        if (importNumber > 0) {
            importsContainer.addFile(file.getName(), importNumber);
        }

        return null;
    }

    private int analyzeForC_Like(String content) {
        int importNumber = 0;
        importNumber += getImportNumberForC_Like(content, "(#(include|import)[ \t]*<[^>]*>)", line ->
        {
            int firstIndex = line.indexOf('<');
            int lastIndex = line.indexOf('>');
            return line.substring(firstIndex + 1, lastIndex);
        });

        importNumber += getImportNumberForC_Like(content, "(#(include|import)[ \t]*\"[^\"]*\")", line ->
        {
            int firstIndex = line.indexOf('\"');
            int lastIndex = line.indexOf('\"', firstIndex + 1);
            return line.substring(firstIndex + 1, lastIndex);
        });

        return importNumber;
    }

    private int getImportNumberForC_Like(String content, String usedPattern,
                                         C_LikeImportExtractor cLikeImportExtractor) {
        int importNumber = 0;
        Pattern pattern = Pattern.compile(usedPattern);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String importLine = content.substring(matcher.start(), matcher.end());
            String importString = cLikeImportExtractor.getImportedHeaderFromLine(importLine);

            int index = importString.lastIndexOf('/');
            importString = importString.substring(++index);

            if (importsContainer.accepts(importString)) {
                importsContainer.addImport(importString);
                importNumber++;

            }
        }
        return importNumber;
    }

    private int analyzeForJava(String content, String filePath) {
        int firstAcc = content.indexOf("{");
        if (firstAcc == -1) {
            log.warn("Java file " + filePath + " has no class / viable content!");
            return 0;
        }
        content = content.substring(0, firstAcc);

        int importNumber = 0;

        Pattern pattern = Pattern.compile("(import [^;]*;)");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String importString = content.substring(matcher.start() + 7, matcher.end() - 1).trim();

            if (importString.indexOf("static ") == 0 || importString.indexOf("static\t") == 0) {
                importString = importString.substring(6).trim();
            }

            if (importsContainer.accepts(importString)) {
                importsContainer.addImport(importString);
                importNumber++;
            }
        }

        return importNumber;
    }

    @Override
    public boolean accepts(String extension) {
        if (language == LibraryDetectorLanguage.JAVA) {
            return languageRegistry.isOfLanguage(JAVA_LANGUAGE, extension);
        }

        if (language == LibraryDetectorLanguage.C_LIKE) {
            return C_LIKE_LANGUAGES.stream().anyMatch(lang -> languageRegistry.isOfLanguage(lang, extension));
        }

        return false;
    }

    public void generateResults() {
        importsContainer.writeFilesWithImports();
        importsContainer.writeImportsToFile();
        importsContainer.writePackagingUnits();
    }

    private interface C_LikeImportExtractor {
        String getImportedHeaderFromLine(String line);
    }
}
