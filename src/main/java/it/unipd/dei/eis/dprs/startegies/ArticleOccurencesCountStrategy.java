package it.unipd.dei.eis.dprs.startegies;

import it.unipd.dei.eis.dprs.BasicArticle;
import it.unipd.dei.eis.dprs.tools.StrategyHelper;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ArticleOccurencesCountStrategy implements WordCountStrategy
{
    public void wordCount(BasicArticle[] basicarticle)
    {
        HashMap<String, Integer> results = new HashMap<>(); //Hashmap dei risultati

        for(BasicArticle b: basicarticle)
        {
            String title = b.getTitle().toLowerCase();
            String body = b.getBody().toLowerCase();
            String article = (title + " " + body).replaceAll("[^a-z+\\s]", ""); //tutto minuscolo per sicurezza

            String[] tokens = article.split("\\s"); //Divido le parole
            Set<String> mySet = new HashSet<>(Arrays.asList(tokens)); //Le inserisco in un set così ne salvo solo una per articolo

            String[] uniqueTokens = new String[mySet.size()];   //Ha senso mettere le parole in un array, poi in un set per prenderne una per articolo e poi rimetterle in un array?
            mySet.toArray(uniqueTokens);

            StrategyHelper.insertIntoMap(uniqueTokens, results);
        }
        try
        {
            StrategyHelper.toTXT(StrategyHelper.sortMap(results));
        }
        catch (IOException e)
        {
            System.err.println("++ERROR. Could not create output file. Details:");
            System.err.println(e.getMessage());
            //throw new RuntimeException(e); gestione delle eccezioni????????????????
        }
    }
}