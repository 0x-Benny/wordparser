package it.unipd.dei.eis.dprs;

import it.unipd.dei.eis.dprs.tools.HelperClass;

import java.io.IOException;
import java.util.*;

public class WordPerArticle implements Strategy{
    @Override
    public HashMap<String, Integer>  wordCount(BasicArticle[] basicarticle, String stoplistPath){
        HashSet<String> stoplist = HelperClass.CreateStopList(stoplistPath);
        HashMap<String, Integer> results = new HashMap<>(); //Hashmap dei risultati

        for(BasicArticle b: basicarticle){  //Finche ci sono articoli nell'array passato
            String title = b.getTitle().toLowerCase();
            String body = b.getBody().toLowerCase();

            String article = title + " " + body; //tutto minuscolo per sicurezza
            article = article.replaceAll("[^a-z+\\s]", ""); //Credo il migliore (?)

            String[] temparticle = article.split("\\s"); //Divido le parole
            Set<String> mySet = new HashSet<>(Arrays.asList(temparticle)); //Le inserisco in un set così ne salvo solo una per articolo

            int n = mySet.size();
            String[] completearticle = new String[n];   //Ha senso mettere le parole in un array, poi in un set per prenderne una per articolo e poi rimetterle in un array?
            int index = 0;                           //Bo per me si
            for(String x : mySet){  //Le copio in un array per facilitare il lavoro
                completearticle[index++] = x;
            }
            HelperClass.insertIntoMap(completearticle, stoplist, results);
        }
        return HelperClass.sortMap(results);
    }
}