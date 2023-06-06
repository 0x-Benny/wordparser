package it.unipd.dei.eis.dprs.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.eis.dprs.BasicArticle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Deserializer
{
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
          articles.add(new BasicArticle(title, body, source));
          //System.out.println("**ARTICOLO CREATO**\nTITOLO: " + title + "\nTESTO: " + body + '\n');
        }
      }
    }
    catch (IOException | NullPointerException e)
    {
      System.err.println("Errore. Database non trovato. Si prega di effettuare prima il download.");
    }
    // Risultato finale
    return articles.toArray(new BasicArticle[0]);
  }
}
