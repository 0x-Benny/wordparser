package it.unipd.dei.eis.dprs;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unipd.dei.eis.dprs.strategies.ArticleOccurrencesCountStrategy;
import it.unipd.dei.eis.dprs.strategies.TotalOccurrencesCountStrategy;
import it.unipd.dei.eis.dprs.strategies.WordCountStrategy;
import it.unipd.dei.eis.dprs.tools.Serializer;
import it.unipd.dei.eis.dprs.tools.StrategyHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * CountStategiesTest è una JUnit test class che verifica tutte le funzionalità della classi
 * {@link StrategyHelper}, {@link ArticleOccurrencesCountStrategy} e {@link TotalOccurrencesCountStrategy}.
 */
public class CountStategiesTest
{
  private static final Random random = new Random();

  /**
   * Metodo ausiliario che pulisce i file di output creati dai test.
   */
  @AfterAll
  static void cleanUp()
  {
    assertTrue(new File("./assets/articlesDB.json").delete());
    assertTrue(new File("./assets/wordCounter.txt").delete());
  }

  /**
   * Metodo ausiliario che crea N parole random formate da 5 a 20 caratteri.
   * @param n Dimensione dell'array.
   * @return Array di stringhe(parole).
   */
  private String[] randomStrings(int n)
  {
    String[] randomStrings = new String[n];
    Random random = new Random();

    for (int i = 0; i < n; i++)
    {
      char[] word = new char[random.nextInt(15) + 5]; // words of length 5 through 20
      for (int j = 0; j < word.length; j++)
      {
        word[j] = (char) ('a' + random.nextInt(26));
      }
      randomStrings[i] = new String(word);
    }
    return randomStrings;
  }

  /**
   * Metodo ausiliario che crea una mappa con n parole e valori random
   * (per testare che funzioni con più e meno di 50 parole) + 4 valori per testare ogni caso.
   * @param n Dimensione della mappa.
   * @return HashMap di parole/valori.
   */
  private HashMap<String, Integer> createRandomMap(int n)
  {
    Random random = new Random();
    HashMap<String, Integer> results = new HashMap<>();

    String[] randomStrings = randomStrings(n); //Metodo creato sopra

    for (int i = 0; i < n; i++)
    {
      results.put(randomStrings[i], random.nextInt(1000));
    }
    results.put("AAAAA", 1100);
    results.put("CCCCC", 1100);
    results.put("BBBBB", 1100);
    results.put("DDDDD", 1100);

    return results;
  }

  /**
   * Metodo ausiliario che crea un array di 5 parole random che non devono essere contate.
   * @return Array di stringhe(parole).
   * @throws IOException -- Lancia IOException se FileReader() non trova la stoplist.
   */
  private String[] arrayBannedWords() throws IOException
  {
    BufferedReader in = new BufferedReader(new FileReader("assets/utilities/english_stoplist_v1.txt"));
    String stopwords;

    List<String> list = new ArrayList<>();
    while ((stopwords = in.readLine()) != null)
    {
      list.add(stopwords);
    }
    String[] tokens = new String[10];
    int count = 0;
    do
    {
      int index = random.nextInt(list.size());
      tokens[count] = list.get(index);
      count++;
    } while (count < 5);

    return tokens;
  }

  /**
   * Metodo ausiliario che crea la lista delle parole che non devono essere contate a partire dalla stoplist fornita.
   * @return Lista di stringhe(parole).
   * @throws IOException -- Lancia IOException se FileReader() non trova la stoplist.
   */
  private List<String> listBannedWords() throws IOException
  {
    BufferedReader in = new BufferedReader(new FileReader("assets/utilities/english_stoplist_v1.txt"));
    String stopwords;

    List<String> list = new ArrayList<>();
    while ((stopwords = in.readLine()) != null)
    {
      list.add(stopwords);
    }
    return list;
  }

  /**
   * Test per controllare che in {@link StrategyHelper#insertIntoMap(String[], HashMap)}
   * le parole non contabili non vengano inserite nella mappa e le altre sì.
   * @throws IOException -- Lancia IOException se arrayBannedWords() non trova la stoplist.
   */
  @Test
  public void bannedWordsInMap() throws IOException
  {
    HashMap<String, Integer> results = new HashMap<>();
    String[] tokens = arrayBannedWords();
    String[] randwords = randomStrings(5);

    System.arraycopy(randwords, 0, tokens, 5, 5);

    StrategyHelper.insertIntoMap(tokens, results);
    String[] wordinmap = results.keySet().toArray(new String[0]);

    List<String> list = listBannedWords();
      for (int i = 0; i < results.size(); i++)
      {
          assertFalse(list.contains(wordinmap[i]), "Test failed, list contains not permitted word");
      }
  }

  /**
   * Test per controllare che in {@link StrategyHelper#sortMap(HashMap)}
   * la mappa ordinata non sia vuota e abbia le dimensioni corrette.
   */
  @Test
  public void finalMapLength()
  {
    int n = random.nextInt(100) + 1;
    HashMap<String, Integer> results = createRandomMap(n);
    results = StrategyHelper.sortMap(results);

    assertFalse(results.isEmpty(), "Test failed cause the hashmap results is empty");
    assertFalse(results.size() > 50, "Test failed cause the hashmap length is > 50");
  }

  /**
   * Test per controllare che in {@link StrategyHelper#sortMap(HashMap)}
   * la mappa venga ordinata in modo corretto (ovvero prima per Value, poi per Key).
   */
  @Test
  public void sortingMap()
  {
    int n = random.nextInt(101);
    HashMap<String, Integer> results = createRandomMap(n);
    results = StrategyHelper.sortMap(results);

    Collection<Integer> values = results.values();
    Integer[] value = values.toArray(new Integer[0]);

    Collection<String> stringhe = results.keySet();
    String[] words = stringhe.toArray(new String[0]);

    for (int i = 0; i < value.length - 1; i++)
    {
      assertFalse(value[i] < value[i + 1],
          "Test failed, value are not sorted in a decreasing way"); //Controllo che i valori siano
      // in ordine decrescente

      //Controllo che in caso di valori uguali si segua l ordine alfabetico
      assertFalse(value[i].equals(value[i + 1]) && words[i].compareTo(words[i + 1]) > 0,
          "Test failed, words with same value aren't sorted following the alphabetical order");
    }
  }

  /**
   * Test per controllare che in {@link StrategyHelper#toTextfile(HashMap)}
   * la mappa venga correttamente trasportata su un file .txt.
   * @throws IOException -- Lancia IOException se non è possibile creare il file di output.
   */
  @Test
  public void textFileDoesntexists() throws IOException
  {
    int n = random.nextInt(101);
    HashMap<String, Integer> results = createRandomMap(n);

    StrategyHelper.toTextfile(results);

    File file = new File("assets/wordCounter.txt");
    assertFalse(!file.exists() || file.length() == 0,
        "Test failed because wordCounter file doesn't exists or it is empty");
  }

  /**
   * Test per controllare che le due classi di {@link WordCountStrategy} contino correttamente le parole.
   * <p>
   * Scrive sul database un articolo generato casualmente (contenete 2 parole ripetute) e testa i 2 metodi di conteggio:
   * - per {@link ArticleOccurrencesCountStrategy} mi aspetto che vengano contate 1 singola volta anche le parole ripetute;
   * - per {@link TotalOccurrencesCountStrategy} mi aspetto che vengano contate 2 volte le parole ripetute.
   * </p>
   * @throws IOException -- Lancia IOException se ci sono errori nella lettura del file di output.
   */
  @Test
  public void strategyTest() throws IOException
  {
    String[] randomwords = randomStrings(5);

    String[] tokens = new String[7];
    System.arraycopy(randomwords, 0, tokens, 0, 5);
    tokens[5] = tokens[0];
    tokens[6] = tokens[1];

    String title = tokens[0] + " " + tokens[1] + " " + tokens[2];
    String body = tokens[3] + " " + tokens[4] + " " + tokens[5] + " " + tokens[6];

    BasicArticle article = new BasicArticle(title, body, "lercio", "www.prova.it");
    BasicArticle[] articles = new BasicArticle[1];
    articles[0] = article;

    Serializer.serialize(articles);

    WordCountStrategy strategy = new ArticleOccurrencesCountStrategy();
    Database.analyze(strategy);

    BufferedReader in = new BufferedReader(new FileReader("assets/wordCounter.txt"));
    String words;
    int[] value = new int[7];

    int index = 0;
    while ((words = in.readLine()) != null)
    {
      words = words.replaceAll("[^0-9]", "");
      value[index] = Integer.parseInt(words);
      index++;
    }

    assertFalse(value[0] != 1 || value[1] != 1,
        "Test failed, wrong counting for ArticleOccurrencesCountStrategy()");

    in = new BufferedReader(new FileReader("assets/wordCounter.txt"));
    strategy = new TotalOccurrencesCountStrategy();
    Database.analyze(strategy);

    index = 0;
    while ((words = in.readLine()) != null)
    {
      words = words.replaceAll("[^0-9]", "");
      value[index] = Integer.parseInt(words);
      index++;
    }
    assertFalse(value[0] != 2 || value[1] != 2,
        "Test failed, wrong counting for TotalOccurencesContStrategy()");
  }
}