package it.unipd.dei.eis.dprs;

import org.apache.commons.cli.*;
import java.io.*;
import java.nio.file.*;

public class Main
{
    public static void main(String[] args)
    {
        Options options = new Options();
        OptionGroup actionGroup = new OptionGroup();
        actionGroup.addOption(new Option("h", "help", false, "Print the help"));
        actionGroup.addOption(new Option("e", "extract-terms", true, "Extract terms from given string"));
        actionGroup.addOption(new Option("d", "download-articles", true, "Download articles from given source"));

        actionGroup.setRequired(true);
        options.addOptionGroup(actionGroup);

        options.addOption(new Option("g", "The Guardian", false, "The Guardian"));
        options.addOption(new Option("n", "The New York Times", false, "The New York Times"));

                // parse
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try
        {
            cmd = parser.parse(options, args);
        }
        catch (org.apache.commons.cli.ParseException ex)
        {
            System.err.println("ERROR - parsing command line:");
            System.err.println(ex.getMessage());
            formatter.printHelp("App -{h,et} [options]", options);
            return;
        }

        if (cmd.hasOption("h"))
        {
            formatter.printHelp("App -{et} [options]", options);
        }
        else if (cmd.hasOption("d"))
        {
            InputStream input = null;
            String url = cmd.getOptionValue("d");
			/* Usare polimorfismo (?) per gestire le sorgenti.
			Se The Guardian, allora:

			TheGuardianAdapter api = new TheGuardianAdapter("9b04d5bd-ae24-4ed7-8bde-7c57ee902f70");
			api.fetchArticles();
			persistenza su file

			Se New York Times, allora:

			CsvToJsonAdapter.csvToJson(fileCSV, fileJSON); (???)
			persistenza su file

			*/
        }
        else if (cmd.hasOption("e"))
        {
            InputStream input = null;
            try
            {
                input = Files.newInputStream(Paths.get(cmd.getOptionValue("e"))); // Riceve il file su cui effettuare l'estrazione

                /* Di seguito, fino a f1.close(), ho incollato codice di prova che avevo utilizzato per collaudare i metodi articlesToObject(),
                filterWords() e wordCount(). L'ho riportato a mero titolo di esempio, in modo da darvi un'idea delle operazioni che
                credo dovrebbero essere svolte invocando da riga di comando il programma con il parametro -e, che serve
                a estrarre i termini e quindi, ho pensato, presuppone che esistano già file su cui lavorare.

                CsvToJsonAdapter.csvToJson(input, output);
                ArrayList<BasicArticle> a = CsvToJsonAdapter.articlesToObject(output);
                ArrayList<String> words = CsvToJsonAdapter.filterWords(a, "english_stoplist_v15.txt");
				Iterator<String> i = words.iterator();
				PrintWriter f = new PrintWriter("filewords.txt");
				while (i.hasNext())
					f.println(i.next());
                PrintWriter f1 = new PrintWriter("frequency.txt");
                TreeMap<String, Integer> frequency = CsvToJsonAdapter.wordCount(words);
				f1.println(frequency.toString());
				f1.close(); */

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                if (input != null)
                {
                    try
                    {
                        input.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
