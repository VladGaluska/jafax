package org.dxworks.dxplatform.plugins.insider;

import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.commons.io.FilenameUtils;
import org.dxworks.dxplatform.plugins.insider.commands.*;
import org.dxworks.dxplatform.plugins.insider.configuration.InsiderConfiguration;
import org.dxworks.dxplatform.plugins.insider.technology.finder.LanguageRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.dxworks.dxplatform.plugins.insider.commands.InsiderCommand.*;
import static org.dxworks.dxplatform.plugins.insider.constants.InsiderConstants.*;

@Slf4j
public class Insider {

    private static HelpCommand helpCommand = new HelpCommand();
    private static VersionCommand versionCommand = new VersionCommand();

    public static void main(String[] args) {

        if (args == null) {
            System.err.println("Arguments cannot be null");
            return;
        }
        if (args.length == 0) {
            System.err.println("No command found");
            helpCommand.execute(null, args);
            return;
        }

        if (versionCommand.parse(args)) {
            versionCommand.execute(null, args);
            return;
        }

        if (helpCommand.parse(args)) {
            helpCommand.execute(null, args);
            return;
        }

        String command = args[0];

        InsiderCommand insiderCommand = getInsiderCommand(command);

        if (insiderCommand == null) {
            System.err.println("Invalid command!");
            helpCommand.execute(null, args);
            return;
        }

        boolean isValidInput = insiderCommand.parse(args);
        if (!isValidInput) {
            log.error("Input is not valid!");
            helpCommand.execute(null, args);
            return;
        }

        if (insiderCommand instanceof NoFilesCommand) {
            insiderCommand.execute(null, args);
        } else {
            List<InsiderFile> insiderFiles = readInsiderConfiguration();
            insiderCommand.execute(insiderFiles, args);
        }

        System.out.println("Insider 1.0 finished analysis");
    }

    private static InsiderCommand getInsiderCommand(String command) {
        switch (command) {
            case FIND:
                return new FindCommand();
            case DETECT:
                return new DetectCommand();
            case ADD:
                return new AddCommand();
            case DIAGNOSE:
                return new DiagnoseCommand();
            case CONVERT:
                return new ConvertCommand();
            default:
                return null;
        }
    }

    private static List<InsiderFile> readInsiderConfiguration() {
        System.out.println("Reading configuration file: " + CONFIGURATION_FILE);

        Path configFilePath = Paths.get(CONFIGURATION_FOLDER, CONFIGURATION_FILE);

        File resultsFolder = new File(RESULTS_FOLDER);
        if (!resultsFolder.exists())
            resultsFolder.mkdirs();


        Properties properties = new Properties();
        try {
            properties.load(new FileReader(configFilePath.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        InsiderConfiguration.loadProperties(properties);

        String rootFolder = InsiderConfiguration.getInstance().getRootFolder();
        reportUnknownExtensions();

        return readProjectFiles(rootFolder);
    }

    private static void reportUnknownExtensions() {
        List<String> requiredLanguages = InsiderConfiguration.getInstance().getListProperty(LANGUAGES);
        requiredLanguages.stream()
                .filter(lang -> !LanguageRegistry.getInstance().containsLanguage(lang))
                .forEach(lang -> System.out.println("Unknown language " + lang));
    }

    private static List<InsiderFile> readProjectFiles(String rootFolder) {
        List<InsiderFile> insiderFiles = new ArrayList<>();
        try {
            List<Path> pathList = Files.walk(Paths.get(rootFolder)).filter(Files::isRegularFile).filter(Insider::hasAcceptedExtension).collect(Collectors.toList());
            try (ProgressBar pb = new ProgressBar("Reading files", pathList.size(), ProgressBarStyle.ASCII)) {
                for (Path path : pathList) {
                    pb.step();
                    try {
                        insiderFiles.add(InsiderFile.builder()
                                .path(path.toAbsolutePath().toString())
                                .name(path.getFileName().toString())
                                .extension(FilenameUtils.getExtension(path.getFileName().toString()))
                                .content(new String(Files.readAllBytes(path)))
                                .build());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return insiderFiles;
    }

    private static boolean hasAcceptedExtension(Path path) {
        String extension = FilenameUtils.getExtension(path.getFileName().toString());
        LanguageRegistry languageRegistry = LanguageRegistry.getInstance();
        List<String> requiredLanguages = InsiderConfiguration.getInstance().getListProperty(LANGUAGES);

        return requiredLanguages.stream()
                .anyMatch(lang -> languageRegistry.isOfLanguage(lang, extension));
    }
}
