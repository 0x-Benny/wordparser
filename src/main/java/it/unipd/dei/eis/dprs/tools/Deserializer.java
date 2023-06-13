package it.unipd.dei.eis.dprs.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.eis.dprs.BasicArticle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Contiene un metodo per deserializzare file JSON.
 */
public class Deserializer
{
  /**
   * Deserializza il file JSON, ovvero mappa gli attributi JSON di ogni articolo nei corrispondenti campi di un oggetto
   * di tipo BasicArticle.
   * @param databasePath Percorso del database (file JSON).
   * @return Array di articoli.
   * @see com.fasterxml.jackson
   */
  public static BasicArticle[] deserialize(String databasePath)
  {
    ArrayList<BasicArticle> articles = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    try
    {
      File articlesDB = new File(databasePath);
      JsonNode database = objectMapper.readTree(articlesDB);
      if (database != null && database.isArray())
      {
        for (JsonNode article : database)
        {
          String title = article.get("title").asText();
          String body = article.get("body").asText();
          String source = article.get("source").asText();
          String url = article.get("url").asText();
          articles.add(new BasicArticle(title, body, source, url));
        }
      }
    }
    catch (IOException | NullPointerException e)
    {
      System.err.println("++ERROR. Database not found. Please, run the \"download\" option first.");
      System.exit(1);
    }
    return articles.toArray(new BasicArticle[0]);
  }
}
