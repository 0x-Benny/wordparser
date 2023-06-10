package it.unipd.dei.eis.dprs;

import it.unipd.dei.eis.dprs.adapters.NYTimesAdapter;
import it.unipd.dei.eis.dprs.adapters.TheGuardianAdapter;
import it.unipd.dei.eis.dprs.tools.Deserializer;
import it.unipd.dei.eis.dprs.tools.HelperClass;
import it.unipd.dei.eis.dprs.tools.Serializer;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.*;

public class Database
{
  public static void download(String query)
  {
    TheGuardianAdapter api = new TheGuardianAdapter("9b04d5bd-ae24-4ed7-8bde-7c57ee902f70", query);
    NYTimesAdapter csv = new NYTimesAdapter("./assets/nytimes/");
    BasicArticle[] database = ArrayUtils.addAll(api.fetchArticles(), csv.fetchArticles());
    Serializer.serialize(database);
  }

  public static void analyze(Strategy strategy)
  {
    BasicArticle[] database = Deserializer.deserialize("./assets/articlesDB.json");

    HashMap<String, Integer> frequency = strategy.wordCount(database, "./assets/utilities/english_stoplist_v1.txt");
    System.out.println(frequency);

    if(frequency.containsKey("")) System.out.println("E' proprio il nulla!");

    try{
      HelperClass.toTXT(frequency);
    }catch (IOException e){System.out.println("Ma non si cancella il txt eddai su");}
  }
}
