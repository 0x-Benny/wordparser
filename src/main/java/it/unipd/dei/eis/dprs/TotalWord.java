package it.unipd.dei.eis.dprs;

import it.unipd.dei.eis.dprs.tools.HelperClass;

import java.util.HashMap;
import java.util.HashSet;

public class TotalWord implements Strategy{
    @Override
    public HashMap<String, Integer> wordCount (BasicArticle[] basicarticle, String stoplistPath)
    {
        HashSet<String> stoplist = HelperClass.CreateStopList(stoplistPath);
        HashMap<String, Integer> results = new HashMap<>(); //Hashmap dei risultati

        for(BasicArticle b: basicarticle){  //Finche ci sono articoli nell'array passato
            String title = b.getTitle().toLowerCase();
            String body = b.getBody().toLowerCase();

            String article = title + " " + body; //tutto minuscolo per sicurezza
            article = article.replaceAll("[^a-z+\\s]", ""); //Credo il migliore (?)

            String[] completearticle = article.split("\\W"); //Divido le parole

            HelperClass.insertIntoMap(completearticle, stoplist, results);
        }
        return HelperClass.sortMap(results);
    }
}