package org.dxworks.dxplatform.plugins.insider.technology.finder.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.Technology;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.json.JsonConfigurationDTO;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.json.TechnologyJsonDTO;
import org.dxworks.dxplatform.plugins.insider.technology.finder.parsers.FingerprintsXmlParser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class XmlToJsonConverter {

    public static void main(String[] args) {
        XmlToJsonConverter xmlToJsonConverter = new XmlToJsonConverter();

        xmlToJsonConverter.convertXmlFileTOJson("config\\libraries.xml");

    }

    public void convertXmlFileTOJson(String xmlFilePath) {
        FingerprintsXmlParser fingerprintsXmlParser = new FingerprintsXmlParser();
        List<Technology> technologies = fingerprintsXmlParser.parseTechnologiesFile(xmlFilePath);

        List<TechnologyJsonDTO> technologyJsonDTOS = technologies.stream().map(TechnologyJsonDTO::fromTechnology).collect(Collectors.toList());

        JsonConfigurationDTO jsonConfigurationDTO = new JsonConfigurationDTO();
        jsonConfigurationDTO.setTechnologies(technologyJsonDTOS);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(xmlFilePath.substring(0, xmlFilePath.lastIndexOf(".xml")) + ".json");
            gson.toJson(jsonConfigurationDTO, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error("Could not write JSON file when converting from XML!", e);
        }
    }
}
