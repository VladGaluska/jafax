package org.dxworks.dxplatform.plugins.insider.technology.finder.parsers;

import org.apache.commons.lang3.NotImplementedException;
import org.dxworks.dxplatform.plugins.insider.technology.finder.exceptions.FingerprintParseException;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.Technology;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.xml.old.FingerprintXmlDTO;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.xml.old.XmlConfigurationDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FingerprintsXmlParser implements FingerprintsParser {

    @Override
    public List<Technology> parseTechnologiesFile(String filePath) {
        Unmarshaller unmarshaller;
        XmlConfigurationDTO xmlConfiguration;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlConfigurationDTO.class);
            unmarshaller = jaxbContext.createUnmarshaller();
            xmlConfiguration = (XmlConfigurationDTO) unmarshaller.unmarshal(Paths.get(filePath).toFile());

        } catch (JAXBException e) {
            throw new FingerprintParseException(e);
        }

        return xmlConfiguration.getData().getLanguages().getLanguages().stream()
                .flatMap(languageDTO -> languageDTO.getTechnologies().stream()
                        .flatMap(technologyDTO -> technologyDTO.getCategories().stream().map(categoryDTO -> {
                            Technology technology = new Technology();
                            technology.setLanguages(Collections.singletonList(languageDTO.getName()));
                            technology.setExtensions(Collections.emptyList());
                            technology.setCategory(technologyDTO.getName());
                            technology.setName(categoryDTO.getName());

                            technology.setFingerprints(categoryDTO.getFingerprints().stream()
                                    .map(FingerprintXmlDTO::getValue)
                                    .collect(Collectors.toList()));

                            return technology;
                        })))
                .collect(Collectors.toList());
    }

    @Override
    public void writeTechnologiesToFile(List<Technology> technologies, Path filePath) throws IOException {
        throw new NotImplementedException("XML is not a supported format for export for the moment!");
    }
}
