package org.dxworks.dxplatform.plugins.insider.commands;

import org.dxworks.dxplatform.plugins.insider.InsiderFile;

import java.util.List;

public class VersionCommand implements NoFilesCommand {
    @Override
    public boolean parse(String[] args) {
        if (args.length != 1)
            return false;

        return VERSION.contains(args[0]);
    }

    @Override
    public void execute(List<InsiderFile> insiderFiles, String[] args) {
        String version = "Insider 1.0.0";

        System.out.println(version);
    }

    @Override
    public String usage() {
        return "insider {-v | -version | --version | version}";
    }
}
