package org.dxworks.dxplatform.plugins.insider.commands;

import lombok.extern.slf4j.Slf4j;
import org.dxworks.dxplatform.plugins.insider.InsiderFile;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.Technology;
import org.dxworks.dxplatform.plugins.insider.technology.finder.parsers.JavaLibrariesCsvParser;
import org.dxworks.dxplatform.plugins.insider.technology.finder.parsers.JsonFingerprintParser;
import org.dxworks.dxplatform.plugins.insider.utils.ImportUtils;
import org.dxworks.dxplatform.plugins.insider.utils.LibraryImport;
import org.dxworks.dxplatform.plugins.insider.utils.LibraryImportInOtherTechnology;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.dxworks.dxplatform.plugins.insider.constants.InsiderConstants.STARS;

@Slf4j
public class AddCommand extends ConvertCommand {

    @Override
    public boolean parse(String[] args) {
        return super.parse(args) && sourceFile.endsWith(".csv") && targetFile.endsWith(".json");
    }

    @Override
    public void execute(List<InsiderFile> insiderFiles, String[] args) {

        try {
            JsonFingerprintParser jsonFingerprintParser = new JsonFingerprintParser();
            List<Technology> jsonTechnologies = jsonFingerprintParser.parseTechnologiesFile(targetFile);

            JavaLibrariesCsvParser javaLibrariesCsvParser = new JavaLibrariesCsvParser();
            List<Technology> csvTechnologies = javaLibrariesCsvParser.parseTechnologiesFile(sourceFile);

            List<LibraryImport> importsAddedToExistingTechnology = new ArrayList<>();
            List<LibraryImport> importsAddedToNewTechnology = new ArrayList<>();
            List<LibraryImport> identicalImportsAlreadyInFile = new ArrayList<>();
            List<LibraryImportInOtherTechnology> importsAlreadyInFileWithDifferentTechnology = new ArrayList<>();
            List<Technology> newlyCreatedTechnologies = new ArrayList<>();

            csvTechnologies.forEach(technology -> {
                List<String> fingerprints = technology.getFingerprints();
                fingerprints.forEach(fingerPrint -> {
                    LibraryImport libraryImport = new LibraryImport(ImportUtils.unwrapImportPackage(fingerPrint), technology.getName());
                    Technology tech = jsonTechnologies.stream().filter(t -> t.getFingerprints().contains(fingerPrint)).findFirst().orElse(null);
                    if (tech != null) { //fingerprint already exists in JSON file
                        if (technology.getName().equalsIgnoreCase(tech.getName())) {
                            identicalImportsAlreadyInFile.add(libraryImport);
                            return;
                        }
                        importsAlreadyInFileWithDifferentTechnology.add(new LibraryImportInOtherTechnology(ImportUtils.unwrapImportPackage(fingerPrint), technology.getName(), tech.getName()));
                        return;
                    }
                    // fingerprint does not exist
                    Technology sameTech = jsonTechnologies.stream().filter(t -> t.getName().equalsIgnoreCase(technology.getName())).findFirst().orElse(null);
                    if (sameTech == null) { //adding to new technology
                        Technology newTechnology = new Technology(technology.getCategory(), technology.getName(), technology.getLanguages(), technology.getExtensions(), Collections.singletonList(fingerPrint));
                        newlyCreatedTechnologies.add(newTechnology);
                        jsonTechnologies.add(newTechnology);
                        importsAddedToNewTechnology.add(libraryImport);
                    } else {
                        List<String> oldFps = sameTech.getFingerprints();
                        List<String> newFps = new ArrayList<>(oldFps);
                        newFps.add(fingerPrint);
                        sameTech.setFingerprints(newFps);
                        if (newlyCreatedTechnologies.contains(sameTech)) {
                            importsAddedToNewTechnology.add(libraryImport);
                        } else {
                            importsAddedToExistingTechnology.add(libraryImport);
                        }
                    }
                });
            });

            jsonFingerprintParser.writeTechnologiesToFile(jsonTechnologies, Paths.get(targetFile));

            printFingerPrintReport(identicalImportsAlreadyInFile, "Fingerprints already present in file to the same Technology (No action taken):");
            printFingerPrintsReport(importsAlreadyInFileWithDifferentTechnology);
            printFingerPrintReport(importsAddedToNewTechnology, "Fingerprints added to a new Technology:");
            printFingerPrintReport(importsAddedToExistingTechnology, "Fingerprints added to an existing Technology:");
        } catch (IOException e) {
            log.error("Could not transform file!", e);
        }
    }

    private void printFingerPrintsReport(List<LibraryImportInOtherTechnology> importsAlreadyInFileWithDifferentTechnology) {
        System.out.println(STARS);
        System.out.println("Fingerprints already present in file, but mapped to a different Technology (No action taken):");
        System.out.println();
        if (importsAlreadyInFileWithDifferentTechnology.isEmpty())
            System.out.println("None Detected");
        else
            System.out.println(importsAlreadyInFileWithDifferentTechnology.stream()
                    .map(libraryImport -> "\t" + String.join(", ", libraryImport.get_import(), libraryImport.getLibrary(), libraryImport.getOtherTechnology()))
                    .collect(Collectors.joining("\n")));
        System.out.println();
    }

    private void printFingerPrintReport(List<LibraryImport> identicalImportsAlreadyInFile, String headerMessage) {
        System.out.println(headerMessage);
        System.out.println(STARS);
        System.out.println();
        if (identicalImportsAlreadyInFile.isEmpty())
            System.out.println("None Detected");
        else
            System.out.println(identicalImportsAlreadyInFile.stream()
                    .map(libraryImport -> "\t" + String.join(", ", libraryImport.get_import(), libraryImport.getLibrary()))
                    .collect(Collectors.joining("\n")));
        System.out.println();
    }

    @Override
    public String usage() {
        return "insider add <path_to_csv> <path_to_json>";
    }
}
