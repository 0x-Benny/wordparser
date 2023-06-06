package it.unipd.dei.eis.dprs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class WordparserTest
{
    public static void main(String[] args) throws IOException
    {
        TheGuardianAdapter api = new TheGuardianAdapter("9b04d5bd-ae24-4ed7-8bde-7c57ee902f70");
        ArrayList<BasicArticle> database = api.fetchArticles();

        NYTimesAdapter csv = new NYTimesAdapter("./assets/nytimes/");
        database.addAll(csv.fetchArticles());

        TreeMap<String, Integer> frequency = Wordparser.wordCount(Wordparser.filterWords(database, "./assets/utilities/english_stoplist_v1.txt"));
        System.out.println(frequency);
    }
}
