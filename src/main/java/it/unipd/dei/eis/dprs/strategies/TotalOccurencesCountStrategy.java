package it.unipd.dei.eis.dprs.strategies;

import it.unipd.dei.eis.dprs.BasicArticle;
import it.unipd.dei.eis.dprs.tools.StrategyHelper;
import java.io.IOException;
import java.util.HashMap;

public class TotalOccurencesCountStrategy implements WordCountStrategy
{
    public void wordCount (BasicArticle[] basicarticle)
    {
        HashMap<String, Integer> results = new HashMap<>(); //Hashmap dei risultati

        for(BasicArticle b: basicarticle)
        {
            String title = b.getTitle().toLowerCase();
            String body = b.getBody().toLowerCase();
            String article = (title + " " + body).replaceAll("[^a-z+\\s]", "");

            String[] tokens = article.split("\\W"); //Divido le parole
            StrategyHelper.insertIntoMap(tokens, results);
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