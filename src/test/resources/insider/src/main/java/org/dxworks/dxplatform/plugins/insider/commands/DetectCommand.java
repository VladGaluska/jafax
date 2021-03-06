package org.dxworks.dxplatform.plugins.insider.commands;

import org.dxworks.dxplatform.plugins.insider.InsiderFile;
import org.dxworks.dxplatform.plugins.insider.library.detector.LibraryDetector;
import org.dxworks.dxplatform.plugins.insider.library.detector.LibraryDetectorLanguage;

import java.util.List;

public class DetectCommand implements InsiderCommand {

    @Override
    public boolean parse(String[] args) {
        if (args.length == 1)
            return true;

        return false;
    }


    @Override
    public void execute(List<InsiderFile> insiderFiles, String[] args) {
        LibraryDetector libraryDetector = new LibraryDetector(LibraryDetectorLanguage.JAVA);
        insiderFiles.stream()
                .filter(insiderFile -> libraryDetector.accepts(insiderFile.getExtension()))
                .forEach(libraryDetector::analyze);

        libraryDetector.generateResults();
    }

    @Override
    public String usage() {
        return "insider detect";
    }
}
