package it.unipd.dei.eis.dprs.tools;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unipd.dei.eis.dprs.BasicArticle;
import java.io.File;
import java.io.IOException;

/**
 * Contiene un metodo per serializzare gli articoli.
 * @see com.fasterxml.jackson
 */

public class Serializer
{
  /**
   * Serializza gli articoli, ovvero mappa un array di articoli in un file JSON.
   * @param articles Array di articoli.
   */
  public static void serialize(BasicArticle[] articles)
  {
    if (articles.length == 0)
    {
      System.err.println("++ERROR. Could not create database. All sources are unavailable.");
      System.exit(1);
    }
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);

			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

      writer.writeValue(new File("./assets/articlesDB.json"), articles);
    }
    catch (IOException e)
    {
      System.err.print("++ERROR. Could not create database. More details: ");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
