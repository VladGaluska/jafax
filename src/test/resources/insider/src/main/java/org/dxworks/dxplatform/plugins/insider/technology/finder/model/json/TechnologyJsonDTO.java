package org.dxworks.dxplatform.plugins.insider.technology.finder.model.json;

import lombok.Data;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.Technology;
import org.dxworks.dxplatform.plugins.insider.utils.ImportUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.dxworks.dxplatform.plugins.insider.utils.ImportUtils.IMPORT_SUFFIX;
import static org.dxworks.dxplatform.plugins.insider.utils.ImportUtils.unwrapImportPackage;

@Data
public class TechnologyJsonDTO {

    private String category;
    private String name;
    private List<String> languages;
    private List<String> extensions;
    private List<String> fingerprints;

    private boolean wrapAsImports;

    public static TechnologyJsonDTO fromTechnology(Technology technology) {
        TechnologyJsonDTO technologyJsonDTO = new TechnologyJsonDTO();

        technologyJsonDTO.setName(technology.getName());
        technologyJsonDTO.setCategory(technology.getCategory());
        technologyJsonDTO.setExtensions(technology.getExtensions());
        technologyJsonDTO.setLanguages(technology.getLanguages());

        AtomicBoolean shouldWrapAsImports = new AtomicBoolean(false);

        List<String> fingerPrints = technology.getFingerprints().stream().map(fingerPrint -> {
            if (fingerPrint.contains(IMPORT_SUFFIX)) {
                shouldWrapAsImports.set(true);
                return unwrapImportPackage(fingerPrint);
            }
            return fingerPrint;
        }).collect(Collectors.toList());

        technologyJsonDTO.setFingerprints(fingerPrints);
        technologyJsonDTO.setWrapAsImports(shouldWrapAsImports.get());

        return technologyJsonDTO;
    }

    public Technology toTechnology() {
        Technology technology = new Technology();

        technology.setName(name);
        technology.setCategory(category);
        technology.setLanguages(languages != null ? languages : Collections.emptyList());
        technology.setExtensions(extensions != null ? extensions : Collections.emptyList());
        technology.setFingerprints(fingerprints == null ? Collections.emptyList() : wrapAsImports ? fingerprintsWrappedAsImports() : fingerprints);

        return technology;
    }

    protected List<String> fingerprintsWrappedAsImports() {
        return fingerprints.stream().map(ImportUtils::wrapImportPackage).collect(Collectors.toList());
    }

}
