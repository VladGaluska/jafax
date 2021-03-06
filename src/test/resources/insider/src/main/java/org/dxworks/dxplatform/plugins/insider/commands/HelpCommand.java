package org.dxworks.dxplatform.plugins.insider.commands;

import org.dxworks.dxplatform.plugins.insider.InsiderFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelpCommand implements NoFilesCommand {
    @Override
    public boolean parse(String[] args) {
        if (args.length != 1)
            return false;

        return HELP.contains(args[0]);
    }

    @Override
    public void execute(List<InsiderFile> insiderFiles, String[] args) {
        String usage = "Insider 1.0  -  usage guide:\n";
        usage += "Configure the source root and the project id in the config/insider-conf.properties file\n\n";

        usage += "This is a list of the commands:\n";

        usage += Stream.of(new HelpCommand(),
                new VersionCommand(),
                new AddCommand(),
                new ConvertCommand(),
                new DiagnoseCommand(),
                new DetectCommand(),
                new FindCommand())
                .map(InsiderCommand::usage)
                .map(s -> "\t" + s)
                .collect(Collectors.joining("\n"));

        usage += "\n\nPlease run insider with the specified commands from the folder you have installed Insider to!\n";

        System.out.println(usage);
    }

    @Override
    public String usage() {
        return "insider {-h | -help | --help | help}";
    }
}
