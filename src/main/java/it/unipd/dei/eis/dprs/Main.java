package it.unipd.dei.eis.dprs;

import it.unipd.dei.eis.dprs.strategies.WordCountStrategy;
import it.unipd.dei.eis.dprs.strategies.TotalOccurrencesCountStrategy;
import it.unipd.dei.eis.dprs.strategies.ArticleOccurrencesCountStrategy;
import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

/**
 * Classe eseguibile del progetto.
 */
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

    HelpFormatter formatter = new HelpFormatter();
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;
    try
    {
      cmd = parser.parse(options, args);
    }
    catch (org.apache.commons.cli.ParseException e)
    {
      System.err.println("++ERROR - Could not parse command line. More details: " + e.getMessage());
      formatter.printHelp("App -{d,de,e,h} <download-query>", options);
      return;
    }

    if (cmd.hasOption('d'))
    {
      Database.download(cmd.getOptionValue('d'));
      System.out.println("**SUCCESS. Download completed. Path: ./assets/articlesDB.json");
    }
    else if (cmd.hasOption("de"))
    {
      Database.download(cmd.getOptionValue("de"));
      System.out.println("**SUCCESS. Download completed. Path: ./assets/articlesDB.json");
      Database.analyze(readChoice());
      System.out.println("**SUCCESS. Extraction completed. Path: ./assets/wordCounter.txt");
    }
    else if (cmd.hasOption('e'))
    {
      Database.analyze(readChoice());
      System.out.println("**SUCCESS. Extraction completed. Path: ./assets/wordCounter.txt");
    }
    else
    {
      formatter.printHelp("App -{d,de,e,h} <download-query>", options);
    }
  }

  /**
   * Metodo ausiliario: in base al valore letto tramite lo scanner, conta le occorrenze complessive di ogni parola
   * oppure il numero di articoli in cui ogni parola compare.
   * @return Una tecnica di conteggio delle parole da adottare nell'analisi dei termini.
   * @see it.unipd.dei.eis.dprs.strategies.WordCountStrategy
   */
  private static WordCountStrategy readChoice()
  {
    Scanner scanner = new Scanner(System.in);
    int choice = 0;
    do
    {
      System.out.println("Select a counting method:" + "\n" +
                          "1 - Count in how many articles the words appear." + "\n" +
                          "2 - Count how many times words appear.");
      String input = scanner.nextLine();
      try
      {
        choice = Integer.parseInt(input);
      }
      catch (NumberFormatException e)
      {
        System.err.println("++ERROR. Please enter a valid integer.");
      }
    } while (choice != 1 && choice != 2);

    if (choice == 1)
    {
      return new ArticleOccurrencesCountStrategy();
    }
    else
    {
      return new TotalOccurrencesCountStrategy();
    }
  }
}