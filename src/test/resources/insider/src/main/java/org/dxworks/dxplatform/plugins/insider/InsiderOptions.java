package org.dxworks.dxplatform.plugins.insider;

import java.io.File;

import static picocli.CommandLine.*;

@Command(name = "insider")
public class InsiderOptions {

    @ArgGroup
    InsiderCommands insiderCommands;

    // @formatter:off
    static class TechnologyFinderOptions {
        @Option(names = {"find", "--find-technologies"},
                description = {"TechnologyFinder is an analysis that find regular expressions",
                        "( that should be provided in a configuration file )",
                        "in your project files."},
                paramLabel = "technology-finder")
        boolean technologyFinderFile;

        @Parameters(description = "FingerPrint Files to analyze")
        File[] fingerPrintFiles;
    }

    static class AddOptions {
        @Option(names = {"add", "--add-imports"},
                description = "The path to the imports CSV file you want to clean.",
                paramLabel = "imports_file")
        boolean add;

        @Parameters(hidden = true)
        File[] files;

        @Parameters(index = "0", description = "The csv file from where add libraries")
        File csvFile;

        @Parameters(index = "1", description = "The json file to add libraries to")
        File jsonFile;
    }

    static class DiagnoseOptions {
        @Option(names = {"diagnose", "-diag", "--diagnose-imports-file"})
        boolean diagnoseFile;

        @Parameters(description = "The path to the imports JSON file you want to diagnose.")
        File jsonFile;
    }

    static class LibraryDetectorOptions {

        @Option(names = {"detect", "--detect-libraries"},
                description = {"Library Detector is a regex based analysis that finds all import statements",
                        "in Java or C-based projects (C, Objective C, C++). It also aggregates the results",
                        "on packages (for Java), or on libraries (through a header to library mapping file for C-based)",
                        "For now, only Java detection is implemented"},
                paramLabel = "library-discovery")
        boolean libraryDiscovery;

        @Parameters(description = "Path to ignored Imports file")
        File ignoredImports;
    }

    static class InsiderCommands {
        @ArgGroup(exclusive = false)
        TechnologyFinderOptions technologyFinderOptions;

        @ArgGroup(exclusive = false)
        LibraryDetectorOptions libraryDetectorOptions;

        @ArgGroup(exclusive = false)
        AddOptions addOptions;

        @ArgGroup(exclusive = false)
        DiagnoseOptions diagnoseOptions;
    }
    // @formatter:on
}
