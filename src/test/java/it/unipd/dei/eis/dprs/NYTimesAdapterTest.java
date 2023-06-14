package it.unipd.dei.eis.dprs;

import static org.junit.jupiter.api.Assertions.*;

import it.unipd.dei.eis.dprs.adapters.NYTimesAdapter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * NYTimesAdapterTest è una JUnit test class che verifica tutte le funzionalità della classe {@link NYTimesAdapter}.
 */
public class NYTimesAdapterTest
{
  private static final String TEST_SOURCE_PATH = "./assets/nytimes/";
  private NYTimesAdapter adapter;

  /**
   * Configura l'ambiente di test inizializzando un'istanza di {@link NYTimesAdapter}.
   */
  @BeforeEach
  public void setUp()
  {
    adapter = new NYTimesAdapter(TEST_SOURCE_PATH);
  }

  /**
   * Verifica il corretto funzionamento del metodo responsabile della ricezione degli articoli.
   * Controlla che restituisca un array di BasicArticle non nullo, non vuoto e con tutti i campi (title, body, source, url) non nulli e correttamente impostati.
   */
  @Test
  public void fetchArticlesTest()
  {
    // Testing the array
    BasicArticle[] articles = adapter.fetchArticles();
    assertNotNull(articles);
    assertEquals(1000, articles.length);

    // Testing the titles
    assertEquals("Hard Questions on Nuclear Power", articles[0].getTitle());
    assertNotNull(articles[250].getTitle());

    // Testing the bodies
    String bodyTest = "? To the Editor: The main reason nuclear power has been in eclipse is that the utilities that own nuclear power plants have done a lousy job of running them. Shaun D. Mullen Newark, Del., May 17, 2005";
    assertEquals(bodyTest, articles[391].getBody());
    assertNotNull(articles[500].getBody());

    // Testing the sources
    assertEquals("NYTimes", articles[77].getSource());
    assertNotNull(articles[750].getBody());

    // Testing the urls
    assertEquals("https://www.nytimes.com/2023/03/30/briefing/nuclear-plant-ukraine-europe.html", articles[777].getUrl());
    assertEquals("http",articles[888].getUrl().substring(0, 4));
    assertNotNull(articles[999].getUrl());
  }

  /**
   * Verifica il corretto funzionamento del metodo responsabile della ricezione degli articoli.
   * Controlla che ritorni un array vuoto senza articoli, dal momento che non è stato possibile trovare le sorgenti.
   * Controlla che venga correttamente stampato il messaggio di errore.
   */
  @Test
  public void fetchArticlesWithNoSourcesTest()
  {
    final ByteArrayOutputStream err = new ByteArrayOutputStream();
    System.setErr(new PrintStream(err));

    NYTimesAdapter nyTimesAdapter = new NYTimesAdapter("invalidPath");
    BasicArticle[] articles = nyTimesAdapter.fetchArticles();

    assertEquals("++WARNING. Missing NYTimes' sources. Continuing..." + System.lineSeparator(), err.toString());
    assertEquals(0, articles.length);
  }
}