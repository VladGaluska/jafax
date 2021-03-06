package org.dxworks.dxplatform.plugins.insider.technology.finder.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.Technology;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.json.JsonConfigurationDTO;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.json.TechnologyJsonDTO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JsonFingerprintParser implements FingerprintsParser {
    @Override
    public List<Technology> parseTechnologiesFile(String filePath) {
        JsonConfigurationDTO configurationDTO = getConfigurationDTO(filePath);
        if (configurationDTO == null)
            return Collections.emptyList();

        return configurationDTO.getTechnologies().stream()
                .map(TechnologyJsonDTO::toTechnology)
                .collect(Collectors.toList());
    }

    @Override
    public void writeTechnologiesToFile(List<Technology> technologies, Path filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(filePath.toFile());
            List<TechnologyJsonDTO> technologyJsonDTOS = technologies.stream().map(TechnologyJsonDTO::fromTechnology).collect(Collectors.toList());
            JsonConfigurationDTO jsonConfigurationDTO = new JsonConfigurationDTO();
            jsonConfigurationDTO.setTechnologies(technologyJsonDTOS);
            gson.toJson(jsonConfigurationDTO, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error("Could not write JSON file!", e);
            throw e;
        }
    }

    public JsonConfigurationDTO getConfigurationDTO(String filePath) {
        Gson gson = new Gson();

        try {
            return gson.fromJson(new FileReader(Paths.get(filePath).toFile()), JsonConfigurationDTO.class);
        } catch (FileNotFoundException e) {
            log.error("Could not read JSON technologies file!", e);
        } catch (JsonSyntaxException e) {
            log.error("File " + filePath + " is could not be parsed as a JSON Technology file!", e);
        }

        return null;
    }
}
