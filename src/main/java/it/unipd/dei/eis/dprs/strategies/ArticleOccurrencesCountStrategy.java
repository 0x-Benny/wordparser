package it.unipd.dei.eis.dprs.strategies;

import it.unipd.dei.eis.dprs.BasicArticle;
import it.unipd.dei.eis.dprs.tools.StrategyHelper;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe che realizza l'interfaccia WordCountStrategy in modo che siano enumerate tutte le occorrenze di ogni parola,
 * escluse eventuali ripetizioni all'interno di uno stesso articolo.
 */
public class ArticleOccurrencesCountStrategy implements WordCountStrategy
{
    /**
     * Conta tutte le occorrenze di ogni parola, escludendo eventuali ripetizioni all'interno di uno stesso articolo.
     * @see it.unipd.dei.eis.dprs.strategies.WordCountStrategy
     */
    public void wordCount(BasicArticle[] articles)
    {
        HashMap<String, Integer> results = new HashMap<>(); //Hashmap dei risultati

        for(BasicArticle b: articles)
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
            StrategyHelper.toTextfile(StrategyHelper.sortMap(results));
        }
        catch (IOException e)
        {
            System.err.println("++ERROR. Could not create output file. Details:");
            System.err.println(e.getMessage());
            //throw new RuntimeException(e); gestione delle eccezioni????????????????
        }
    }
}