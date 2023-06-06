package it.unipd.dei.eis.dprs.adapters;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import it.unipd.dei.eis.dprs.BasicArticle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NYTimesAdapter
{
  private final String sourcePath;
  public ArrayList<BasicArticle> articles = new ArrayList<>();

  public NYTimesAdapter(final String sourcePath)
  {
    this.sourcePath = sourcePath;
  }

  public BasicArticle[] fetchArticles()
  {
    File[] files = new File(sourcePath).listFiles();
    if (files != null)
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
            articles.add(new BasicArticle(article.get(2), article.get(3), "NYTimes"));
            //System.out.println("**ARTICOLO CREATO**\nTITOLO: " + article.get(2) + "\nTESTO: " + article.get(3) + '\n');
          }
        }
        catch (IOException e)
        {
          System.err.println("++ERRORE. Lettura del file non riuscita. Dettagli:");
          e.printStackTrace();
        }
      }
    }
    else
      System.out.println("++ERRORE. Sorgenti NYTimes inesistenti.");
    // Risultato finale
    return articles.toArray(new BasicArticle[0]);
  }
}
