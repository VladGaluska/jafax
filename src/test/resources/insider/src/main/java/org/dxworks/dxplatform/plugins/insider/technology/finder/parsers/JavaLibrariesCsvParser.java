package org.dxworks.dxplatform.plugins.insider.technology.finder.parsers;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.dxworks.dxplatform.plugins.insider.technology.finder.exceptions.FingerprintParseException;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.Technology;
import org.dxworks.dxplatform.plugins.insider.utils.ImportUtils;
import org.dxworks.dxplatform.plugins.insider.utils.LibraryImport;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JavaLibrariesCsvParser implements FingerprintsParser {

    @Override
    public List<Technology> parseTechnologiesFile(String filePath) throws FingerprintParseException {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(filePath));
            Map<String, List<LibraryImport>> librariesImportsMap = csvReader.readAll().stream()
                    .filter(strings -> {
                        if (strings.length != 2) {
                            log.error("Invalid input line for package " + strings.toString());
                            return false;
                        }
                        if (strings[0].trim().isEmpty() || strings[1].trim().isEmpty()) {
                            log.error("Invalid input line for package " + strings.toString());
                            return false;
                        }

                        return true;
                    })
                    .map(strings -> new LibraryImport(strings[0].trim(), strings[1].trim()))
                    .collect(Collectors.groupingBy(LibraryImport::getLibrary));

            return librariesImportsMap.entrySet().stream()
                    .map(stringListEntry -> new Technology("External Libraries",
                            stringListEntry.getKey(),
                            Collections.singletonList("java"),
                            Collections.emptyList(),
                            stringListEntry.getValue().stream()
                                    .map(LibraryImport::get_import)
                                    .map(ImportUtils::wrapImportPackage)
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Could not read JSON technologies file!", e);
        }
        return Collections.emptyList();
    }

    @Override
    public void writeTechnologiesToFile(List<Technology> technologies, Path filePath) throws IOException {
        List<String[]> stringArrays = technologies.stream().flatMap(technology -> technology.getFingerprints().stream()
                .map(fingerPrint -> new String[]{ImportUtils.unwrapImportPackage(fingerPrint), technology.getName()}))
                .collect(Collectors.toList());

        CSVWriter writer = new CSVWriter(new FileWriter(filePath.toString()));
        writer.writeAll(stringArrays);
        writer.flush();
        writer.close();
    }
}

