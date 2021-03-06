package org.dxworks.dxplatform.plugins.insider.commands;

import org.dxworks.dxplatform.plugins.insider.InsiderFile;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.Technology;
import org.dxworks.dxplatform.plugins.insider.technology.finder.parsers.JsonFingerprintParser;
import org.dxworks.dxplatform.plugins.insider.utils.ImportUtils;
import org.dxworks.dxplatform.plugins.insider.utils.LibraryImport;
import org.dxworks.dxplatform.plugins.insider.utils.LibraryImportInOtherTechnology;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiagnoseCommand implements NoFilesCommand {

    private String file;

    @Override
    public boolean parse(String[] args) {
        if (args.length != 2)
            return false;

        file = args[1];

        return fileExists(file);
    }

    @Override
    public void execute(List<InsiderFile> insiderFiles, String[] args) {

        JsonFingerprintParser jsonFingerprintParser = new JsonFingerprintParser();
        List<Technology> jsonTechnologies = jsonFingerprintParser.parseTechnologiesFile(file);

        List<String> duplicatedTechnologies = jsonTechnologies.stream()
                .collect(Collectors.groupingBy(Technology::getName, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println("Duplicated technologies: " + duplicatedTechnologies.toString());

        List<LibraryImportInOtherTechnology> duplicatedFingerPrints = new ArrayList<>();

        jsonTechnologies.forEach(technology -> {
            List<String> fingerprints = technology.getFingerprints();
            fingerprints.forEach(fingerprint -> {
                jsonTechnologies.forEach(otherTechnology -> {
                    if (technology.equals(otherTechnology))
                        return;
                    if (otherTechnology.getFingerprints().contains(fingerprint)) {
                        duplicatedFingerPrints.add(new LibraryImportInOtherTechnology(fingerprint, technology.getName(), otherTechnology.getName()));
                    }
                });
            });
        });

        System.out.println("Duplicated Fingerprints: " + duplicatedFingerPrints.stream()
                .distinct()
                .map(LibraryImport::get_import)
                .map(ImportUtils::unwrapImportPackage)
                .collect(Collectors.toList()));
    }

    @Override
    public String usage() {
        return "insider diagnose <path_to_json>";
    }
}
