package it.unipd.dei.eis.dprs.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe ausiliaria. Contiene metodi per la gestione di una lista di stop words e la creazione
 * di una mappa di coppie parola-frequenza.
 */
public class StrategyHelper
{
    /**
     * Crea una lista di stop words, ovvero di parole da escludere dal computo.
     * @return Insieme di parole gestito tramite tabella hash.
     */
    private static HashSet<String> createStopList()
    {
        //Set con le parole non volute
        HashSet<String> stoplist = new HashSet<>();
        try
        {
            List<String> allLines = Files.readAllLines(Paths.get("./assets/utilities/english_stoplist_v1.txt"));
            stoplist.addAll(allLines);
            stoplist.add("");
        }
        catch(IOException e)
        {
            System.err.println("++WARNING. Stoplist file not found. Proceeding without it.");
        }
        return stoplist;
    }

    /**
     * Inserisce in una mappa un insieme di parole, ciascuna con la rispettiva frequenza.
     * @param tokens Insieme di parole da inserire nella mappa.
     * @param results Mappa di coppie parola-frequenza
     */
    public static void insertIntoMap(String[] tokens, HashMap<String, Integer> results)
    {
        HashSet<String> stoplist = StrategyHelper.createStopList();
        for (String word : tokens)
        {
            //Se la parole non è tra quelle brutte
            if (!stoplist.contains(word))
            {
                //e la mia hashmap non contiene già la parola
                if (!results.containsKey(word) && word != null)
                {
                    //allora la inserisco e le do il valore 1
                    results.put(word, 1);
                }
                //Altrimenti se la contiene
                else if (results.containsKey(word) && word != null)
                {
                    //ne aumento solo il valore
                    results.put(word, results.get(word) + 1);
                }
            }
        }
    }

    /**
     * Ordina una mappa di coppie parola-frequenza.
     * @param results Mappa di coppie parola-frequenza.
     * @return Mappa di coppie parola-frequenza ordinate in base alla frequenza.
     */
    public static HashMap<String, Integer> sortMap (HashMap<String, Integer> results)
    {
        ArrayList<Integer> list = new ArrayList<>();
        LinkedHashMap<String, Integer> sortedmap = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : results.entrySet())
        {
            list.add(entry.getValue());
        }

        list.sort(Collections.reverseOrder());
        ArrayList<Integer> first50element = (ArrayList<Integer>) list.stream().limit(50).collect(Collectors.toList());

        for (Integer num : first50element)
        {
            for (Map.Entry<String, Integer> entry : results.entrySet())
            {
                if (entry.getValue().equals(num))
                {
                    sortedmap.put(entry.getKey(), num);
                }
            }
        }
        return sortedmap;
    }

    /**
     * Esporta in un file di testo una mappa di coppie parola-frequenza.
     * @param results Mappa di coppie parola-frequenza.
     * @throws IOException Lancia l'eccezione se si verificano errori nell'accesso alla memoria di massa.
     */
    public static void toTextfile(HashMap<String, Integer> results) throws IOException
    {
        String result = String.valueOf(results).replaceAll(",","\n").replaceAll("[^a-zA-Z0-9_+=+\\n]","");
        try (Writer out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get("./assets/wordCounter.txt")), StandardCharsets.UTF_8)))
        {
            out.write(result);
        }
    }
}
