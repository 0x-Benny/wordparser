package it.unipd.dei.eis.dprs.adapters;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import it.unipd.dei.eis.dprs.BasicArticle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce l'accesso ad articoli del New York Times.
 * @see it.unipd.dei.eis.dprs.adapters.SourceAdapter
 */
public class NYTimesAdapter implements SourceAdapter
{
  // Percorso della sorgente.
  private final String sourcePath;
  // Collezione di articoli.
  public ArrayList<BasicArticle> articles = new ArrayList<>();

  public NYTimesAdapter(final String sourcePath)
  {
    this.sourcePath = sourcePath;
  }

  /**
   * Preleva gli articoli.
   * @return Array di articoli.
   * @see com.fasterxml.jackson.databind.MappingIterator
   * @see com.fasterxml.jackson.dataformat
   */
  public BasicArticle[] fetchArticles()
  {
    File[] files = new File(sourcePath).listFiles();
    if (files != null && files.length != 0)
    {
      for (File file : files)
      {
        //Lettura del csv
        CsvMapper csvMapper = new CsvMapper();
        try
        {
          MappingIterator<List<String>> mappingIterator = csvMapper
              .readerForListOf(String.class)
              .with(CsvParser.Feature.WRAP_AS_ARRAY)
              .readValues(file);
          while (mappingIterator.hasNextValue())
          {
            List<String> article = mappingIterator.nextValue();
            articles.add(new BasicArticle(article.get(2), article.get(3), "NYTimes", article.get(1)));
            //System.out.println("**ARTICOLO CREATO**\nTITOLO: " + article.get(2) + "\nTESTO: " + article.get(3) + '\n');
          }
        }
        catch (IOException e)
        {
          System.err.println("++ERROR. Could not read files. More details:");
          System.err.println(e.getMessage());
        }
      }
    }
    else
      System.err.println("++ERROR. Missing NYTimes' sources.");
    // Risultato finale
    return articles.toArray(new BasicArticle[0]);
  }
}
