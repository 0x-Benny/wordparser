package it.unipd.dei.eis.dprs;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public class Main
{
  public static void main(String[] args)
  {
    Options options = new Options();
    OptionGroup actionGroup = new OptionGroup();

    actionGroup.addOption(new Option("h", "help", false, "Print this help."));
    actionGroup.addOption(new Option("e", "extract-terms", false, "Extract terms from internal database."));
    actionGroup.addOption(new Option("d", "download-articles", true, "Download articles from all sources."));
    actionGroup.addOption(new Option("de", "download-and-extract", true, "Download articles and extract terms sequentially."));
    actionGroup.setRequired(true);

    options.addOptionGroup(actionGroup);

    // parse
    HelpFormatter formatter = new HelpFormatter();
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;
    try
    {
      cmd = parser.parse(options, args);
    }
    catch (org.apache.commons.cli.ParseException e)
    {
      System.err.println("++ERROR - Could not parse command line. More details:");
      System.err.println(e.getMessage());
      formatter.printHelp("App -{d,de,e,h} <download-query>", options);
      return;
    }

    if (cmd.hasOption('d'))
    {
      Database.download(cmd.getOptionValue('d'));
      System.out.println("**SUCCESS. Download completed.");
    }
    else if (cmd.hasOption("de"))
    {
      Database.download(cmd.getOptionValue("de"));
      System.out.println("**SUCCESS. Download completed.");
      Database.analyze();
      System.out.println("**SUCCESS. Extraction completed.");
    }
    else if (cmd.hasOption('e'))
    {
      Database.analyze();
      System.out.println("**SUCCESS. Extraction completed.");
    }
    else
    {
      formatter.printHelp("App -{d,de,e,h} <download-query>", options);
    }
  }
}
