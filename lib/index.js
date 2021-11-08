const {jafax} = require("./lib");
const {Command} = require("commander");

exports.jafaxCommand = new Command()
  .name('jafax')
  .description('Run the JaFaX tool')
  .option('-wd --working-directory', 'Selects the directory where JaFaX will store the results folder.' +
    ` Defaults to the location where JaFaX is installed: ${__dirname}. If set to true it will use the current working directory process.cwd()`,
    false)
  .allowUnknownOption()
  .action(jafax)


