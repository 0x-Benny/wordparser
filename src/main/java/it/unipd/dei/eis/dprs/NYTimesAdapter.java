package it.unipd.dei.eis.dprs;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

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

  public ArrayList<BasicArticle>  fetchArticles() throws IOException
  {
    File[] files = new File(sourcePath).listFiles();
    assert files != null;
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
          System.out.println("**ARTICOLO CREATO**\nTITOLO: " + article.get(2) + "\nTESTO: " + article.get(3) + '\n');
        }
      }
      catch (IOException e)
      {
        System.out.println("Errore.");
        e.printStackTrace();
      }
    }

    // Risultato finale
    return articles; //.toArray(new BasicArticle[0]);
  }
}
