package org.dxworks.dxplatform.plugins.insider.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.dxworks.dxplatform.plugins.insider.InsiderFile;
import org.dxworks.dxplatform.plugins.insider.InsiderResult;
import org.dxworks.dxplatform.plugins.insider.configuration.InsiderConfiguration;
import org.dxworks.dxplatform.plugins.insider.constants.InsiderConstants;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.Technology;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.json.JsonConfigurationDTO;
import org.dxworks.dxplatform.plugins.insider.technology.finder.parsers.JsonFingerprintParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.dxworks.dxplatform.plugins.insider.constants.InsiderConstants.RESULTS_FOLDER;

@Slf4j
public class FindCommand implements InsiderCommand {

    private List<String> fingerprintFiles;

    @Override
    public boolean parse(String[] args) {
        if (args.length == 1) {
            return false;
        }

        String[] files = Arrays.copyOfRange(args, 1, args.length);
        fingerprintFiles = Arrays.stream(files).filter(this::fileExists).collect(Collectors.toList());

        return !fingerprintFiles.isEmpty() && files.length == fingerprintFiles.size();
    }

    @Override
    public void execute(List<InsiderFile> insiderFiles, String[] args) {
        JsonFingerprintParser parser = new JsonFingerprintParser();

        fingerprintFiles.forEach(file -> {
            JsonConfigurationDTO configurationDTO = parser.getConfigurationDTO(file);

            List<Technology> technologies = parser.parseTechnologiesFile(file);

            List<InsiderResult> insiderResults;

            try (ProgressBar pb = new ProgressBarBuilder()
                    .setInitialMax(insiderFiles.size())
                    .setUnit("Files", 1)
                    .setTaskName("Matching")
                    .setStyle(ProgressBarStyle.ASCII)
                    .setUpdateIntervalMillis(100)
                    .setPrintStream(System.err)
                    .build()) {
                insiderResults = insiderFiles.parallelStream()
                        .flatMap(insiderFile ->
                        {
                            Stream<InsiderResult> insiderResultStream = technologies.parallelStream()
                                    .map(technology -> technology.analyze(insiderFile))
                                    .filter(Objects::nonNull)
                                    .filter(insiderResult -> insiderResult.getValue() > 0);
                            pb.step();
                            return insiderResultStream;
                        })
                        .collect(Collectors.toList());
            }
            int sum = insiderResults.stream().mapToInt(InsiderResult::getValue).sum();

            System.out.println(sum);

            String outputFileName = configurationDTO.getOutputFileName();
            if (outputFileName == null) {
                outputFileName = InsiderConfiguration.getInstance().getProperty(InsiderConstants.PROJECT_ID) + "-" + file.substring(file.lastIndexOf(File.separator) + 1);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                FileWriter writer = new FileWriter(Paths.get(RESULTS_FOLDER, outputFileName).toFile());
                gson.toJson(insiderResults, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                log.error("Could not write JSON file when converting from XML!", e);
            }
        });
    }

    @Override
    public String usage() {
        return "insider find <path_to_json>...";
    }
}
