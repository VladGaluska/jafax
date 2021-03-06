package org.dxworks.dxplatform.plugins.insider.technology.finder;

import org.dxworks.dxplatform.plugins.insider.InsiderAnalysis;
import org.dxworks.dxplatform.plugins.insider.InsiderFile;
import org.dxworks.dxplatform.plugins.insider.InsiderResult;
import org.dxworks.dxplatform.plugins.insider.technology.finder.model.Technology;

import java.util.List;

public class TechnologyFinder implements InsiderAnalysis {

    private List<String> fingerprintFiles;
    private List<Technology> technologies;

    public TechnologyFinder(List<String> fingerprintFiles) {
        this.fingerprintFiles = fingerprintFiles;
        loadFingerPrints();
    }

    private void loadFingerPrints() {

    }

    @Override
    public InsiderResult analyze(InsiderFile insiderFile) {
        return null;
    }

    @Override
    public boolean accepts(String extension) {
        return false;
    }
}
