package org.dxworks.dxplatform.plugins.insider;

public interface InsiderAnalysis {
    InsiderResult analyze(InsiderFile insiderFile);

    boolean accepts(String extension);
}
