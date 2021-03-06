package org.dxworks.dxplatform.plugins.insider.technology.finder;

import org.dxworks.dxplatform.plugins.insider.exceptions.InsiderException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class LanguageRegistry {

    public static final String LANGUAGES_TO_EXTENSIONS_CONFIG_FILE_NAME = "languageToExtensions.properties";
    private static LanguageRegistry _instance = new LanguageRegistry();
    private Map<String, List<String>> languagesToExtensionsMap = new HashMap<>();

    private LanguageRegistry() {
        loadLanguagesToExtensionsFromConfigurationFile();
    }

    public static LanguageRegistry getInstance() {
        return _instance;
    }

    private void loadLanguagesToExtensionsFromConfigurationFile() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(Paths.get("config", LANGUAGES_TO_EXTENSIONS_CONFIG_FILE_NAME).toFile()));
        } catch (IOException e) {
            throw new InsiderException(
                    "Could not load extension to languages mapping file. Please make sure a file called "
                            + "languageToExtensions.properties exists in the Insider installation directory.");
        }
        properties.forEach(
                (key, value) -> languagesToExtensionsMap.put((String) key, Arrays.asList(((String) value).split(","))));
        System.out.println("Read language to extensions mapping");
    }

    public boolean isOfLanguage(String language, String extension) {
        List<String> extensions = languagesToExtensionsMap.get(language);
        if (extensions == null)
            return false;

        return extensions.contains(extension);
    }

    public boolean containsLanguage(String lang) {
        return languagesToExtensionsMap.containsKey(lang);
    }
}
