package it.unipd.dei.eis.dprs;

import static org.junit.jupiter.api.Assertions.*;

import it.unipd.dei.eis.dprs.adapters.TheGuardianAdapter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import okhttp3.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * TheGuardianAdapterTest è una JUnit test class che verifica tutte le fnzionalità della classe {@link TheGuardianAdapter}.
 */
class TheGuardianAdapterTest
{
  private static final String QUERY = "test";
  private TheGuardianAdapter adapter;

  /**
   * Configura l'ambiente di test inizializzando un'istanza di {@link TheGuardianAdapter}.
   */
  @BeforeEach
  void setUp()
  {
    adapter = new TheGuardianAdapter(QUERY);
  }

  /**
   * Verifica il corretto funzionamento del metodo ausiliario di formattazione con 4 test case differenti.
   * Essendo originariamente un metodo privato, utilizza la riflessione per testarne il funzionamento.
   * @throws NoSuchMethodException -- Viene lanciata NoSuchMethodException se non viene trovato il metodo dichiarato.
   * @throws InvocationTargetException -- Viene lanciata InvocationTargetException se il metodo inovato lancia a sua volta un'eccezione.
   * @throws IllegalAccessException -- Viene lanciata IllegalAccessException se il metodo invocato è inaccessibile.
   * @see java.lang.reflect
   */
  @Test
  void bodyFormatterTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
  {
    Method bodyFormatter_localTest = TheGuardianAdapter.class.getDeclaredMethod("bodyFormatter", String.class);
    bodyFormatter_localTest.setAccessible(true);

    // Test when text is null
    String inputText1 = null;
    assertNull(bodyFormatter_localTest.invoke(null, inputText1));

    // Test when text is empty
    String inputText2 = "";
    assertEquals("", bodyFormatter_localTest.invoke(null, inputText2));

    // Test when text contains only HTML tags
    String inputText3 = "<p><br><span></span></p>";
    assertEquals("", bodyFormatter_localTest.invoke(null, inputText3));

    // Test when text contains HTML tags and text
    String inputText4 = "<p>This is a <b>test</b> text.</p>";
    String expectedOutput = "This is a test text.";
    assertEquals(expectedOutput, bodyFormatter_localTest.invoke(null, inputText4));

    bodyFormatter_localTest.setAccessible(false);
  }

  /**
   * Verifica il corretto funzionamento del metodo ausiliario di creazione della richiesta.
   * Essendo originariamente un metodo privato, utilizza la riflessione per testarne il funzionamento.
   * @throws NoSuchMethodException -- Viene lanciata NoSuchMethodException se non viene trovato il metodo dichiarato.
   * @throws InvocationTargetException -- Viene lanciata InvocationTargetException se il metodo inovato lancia a sua volta un'eccezione.
   * @throws IllegalAccessException -- Viene lanciata IllegalAccessException se il metodo invocato è inaccessibile.
   * @see java.lang.reflect
   * @see okhttp3.Request
   */
  @Test
  void requestBuilderTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
  {
    Method requestBuilder_localTest = TheGuardianAdapter.class.getDeclaredMethod("requestBuilder");
    requestBuilder_localTest.setAccessible(true);
    Request request = (Request) requestBuilder_localTest.invoke(adapter);
    requestBuilder_localTest.setAccessible(false);

    Method keyLoader_localTest = TheGuardianAdapter.class.getDeclaredMethod("keyLoader");
    keyLoader_localTest.setAccessible(true);
    String apiKey = (String) keyLoader_localTest.invoke(null);
    keyLoader_localTest.setAccessible(false);

    String expectedUrl = "http://content.guardianapis.com/search?api-key=" + apiKey + "&show-fields=body&page-size=200&q=test";
    assertEquals(expectedUrl, request.url().toString());
  }

  /**
   * Verifica il corretto funzionamento del metodo responsabile della ricezione degli articoli.
   * Controlla che restituisca un array di BasicArticle non nullo, non vuoto e con tutti i campi (title, body, source, url) non nulli.
   */
  @Test
  void fetchArticlesTest()
  {
    // Testing the array
    BasicArticle[] articles = adapter.fetchArticles();
    assertNotNull(articles);
    assertTrue(articles.length > 0);

    // Testing the titles
    assertTrue(articles[0].getTitle().toLowerCase().contains("test"));
    assertNotNull(articles[25].getTitle());

    // Testing the bodies
    assertTrue(articles[50].getBody().toLowerCase().contains("test"));
    assertNotNull(articles[75].getBody());

    // Testing the sources
    assertEquals("TheGuardian", articles[100].getSource());
    assertNotNull(articles[125].getSource());

    // Testing the urls
    assertEquals("http",articles[150].getUrl().substring(0, 4));
    assertNotNull(articles[199].getUrl());
  }
}