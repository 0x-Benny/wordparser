package it.unipd.dei.eis.dprs;

import it.unipd.dei.eis.dprs.adapters.NYTimesAdapter;
import it.unipd.dei.eis.dprs.adapters.TheGuardianAdapter;
import it.unipd.dei.eis.dprs.tools.Deserializer;
import it.unipd.dei.eis.dprs.tools.Serializer;
import org.apache.commons.lang3.ArrayUtils;

public class Database
{
  public static void download(String query)
  {
    TheGuardianAdapter api = new TheGuardianAdapter("9b04d5bd-ae24-4ed7-8bde-7c57ee902f70", query);
    NYTimesAdapter csv = new NYTimesAdapter("./assets/nytimes/");
    BasicArticle[] database = ArrayUtils.addAll(api.fetchArticles(), csv.fetchArticles());
    Serializer.serialize(database);
  }

  public static void analyze()
  {
    BasicArticle[] database = Deserializer.deserialize("./assets/articlesDB.json");
    //TreeMap<String, Integer> frequency = Wordparser.wordCount(Wordparser.filterWords(database, "./assets/utilities/english_stoplist_v1.txt"));
    //System.out.println(frequency);
  }
}
