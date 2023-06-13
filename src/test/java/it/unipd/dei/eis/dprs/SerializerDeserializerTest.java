package it.unipd.dei.eis.dprs;

import static org.junit.jupiter.api.Assertions.*;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import it.unipd.dei.eis.dprs.strategies.WordCountStrategy;
import it.unipd.dei.eis.dprs.tools.Deserializer;
import it.unipd.dei.eis.dprs.tools.Serializer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * SerializerDeserializerTest è una JUnit test class che verifica tutte le funzionalità delle classi {@link Serializer} e {@link Deserializer}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SerializerDeserializerTest
{
  /**
   * Verifica che in caso tutte le sorgenti non siano disponibili, il programma avvisi correttamente l'utente e termini l'esecizione.
   * @throws Exception -- SystemLambda può lanciare una generica Exception.
   * @see SystemLambda
   */
  @Test
  @Order(1)
  public void serializeWithEmptyArrayTest() throws Exception
  {
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    System.setErr(new PrintStream(err));
    BasicArticle[] articles = new BasicArticle[0];
    int status = SystemLambda.catchSystemExit(() -> Serializer.serialize(articles));

    assertEquals("++ERROR. Could not create database. All sources are unavailable.\n", err.toString());
    assertEquals(1, status);
  }

  /**
   * Verifica che in caso si chiami {@link Database#analyze(WordCountStrategy)} senza aver prima effettuato {@link Database#download(String)},
   * il programma avvisi correttamente l'utente e termini l'esecuzione.
   * @throws Exception -- SystemLambda può lanciare una generica Exception.
   * @see SystemLambda
   */
  @Test
  @Order(2)
  public void deserializeNonExistingDatabaseTest() throws Exception
  {
    String databasePath = "path/to/non/existing/database.json";
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    System.setErr(new PrintStream(err));
    int status = SystemLambda.catchSystemExit(() -> Deserializer.deserialize(databasePath));

    assertEquals("++ERROR. Database not found. Please, run the \"download\" option first.\n", err.toString());
    assertEquals(1, status);
  }

  /**
   * Verifica che venga correttamente creato il Database serializzando l'array di articoli passato come argomento.
   */
  @Test
  @Order(3)
  public void serializeWithValidArrayTest()
  {
    BasicArticle[] articles = new BasicArticle[2];
    articles[0] = new BasicArticle("title1", "body1", "source1", "url1");
    articles[1] = new BasicArticle("title2", "body2", "source2", "url2");
    Serializer.serialize(articles);

    File database = new File("./assets/articlesDB.json");
    assertTrue(database.exists());
  }

  /**
   * Verifica che venga correttamente deserializzato il Database controllando che tutti i campi (title, body, source, url) corrispondano.
   */
  @Test
  @Order(4)
  public void deserializeWithValidDatabaseTest()
  {
    String databasePath = "./assets/articlesDB.json";
    BasicArticle[] articles = Deserializer.deserialize(databasePath);
    assertNotNull(articles);
    assertEquals(2, articles.length);

    BasicArticle article1 = articles[0];
    assertNotNull(article1);
    assertEquals("title1", article1.getTitle());
    assertEquals("body1", article1.getBody());
    assertEquals("source1", article1.getSource());
    assertEquals("url1", article1.getUrl());

    BasicArticle article2 = articles[1];
    assertNotNull(article2);
    assertEquals("title2", article2.getTitle());
    assertEquals("body2", article2.getBody());
    assertEquals("source2", article2.getSource());
    assertEquals("url2", article2.getUrl());

    assertTrue(new File("./assets/articlesDB.json").delete());
  }
}