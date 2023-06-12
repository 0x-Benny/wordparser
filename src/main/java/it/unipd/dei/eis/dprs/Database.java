package it.unipd.dei.eis.dprs;

import it.unipd.dei.eis.dprs.adapters.NYTimesAdapter;
import it.unipd.dei.eis.dprs.adapters.TheGuardianAdapter;
import it.unipd.dei.eis.dprs.strategies.WordCountStrategy;
import it.unipd.dei.eis.dprs.tools.Deserializer;
import it.unipd.dei.eis.dprs.tools.Serializer;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Gestisce la persistenza su file degli articoli e l'estrazione dei termini.
 */
public class Database
{
  /**
   * Scarica gli articoli da una sorgente (per semplicità, nella presente implementazione da entrambe le sorgenti
   * di riferimento) e li memorizza in un file JSON.
   * @param query Termine di ricerca.
   * @see Serializer
   */
  public static void download(String query)
  {
    TheGuardianAdapter api = new TheGuardianAdapter("9b04d5bd-ae24-4ed7-8bde-7c57ee902f70", query);
    NYTimesAdapter csv = new NYTimesAdapter("./assets/nytimes/");
    BasicArticle[] database = ArrayUtils.addAll(api.fetchArticles(), csv.fetchArticles());
    Serializer.serialize(database);
  }

  /**
   * Analizza gli articoli per il conteggio delle parole, utilizzando la tecnica specificata come parametro.
   * @param strategy Tecnica di conteggio da utilizzare.
   * @see WordCountStrategy
   */
  public static void analyze(WordCountStrategy strategy)
  {
    BasicArticle[] database = Deserializer.deserialize("./assets/articlesDB.json");
    strategy.wordCount(database);
  }
}
