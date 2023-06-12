package it.unipd.dei.eis.dprs.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
            // Ogni parola assente dalla lista di stop words,
            if (!stoplist.contains(word))
            {
                // e assente dalla mappa,
                if (!results.containsKey(word) && word != null)
                {
                    // viene inserita nella mappa con frequenza 1;
                    results.put(word, 1);
                }
                // se la parola è già presente nella mappa
                else if (results.containsKey(word) && word != null)
                {
                    // allora la sua frequenza è incrementata di un'unità
                    results.put(word, results.get(word) + 1);
                }
            }
        }
    }

    /**
     * Ordina una mappa di coppie parola-frequenza.
     * @param results Mappa di coppie parola-frequenza.
     * @return Mappa di coppie parola-frequenza ordinate prima in base alla frequenza e poi per ordine alfabetico.
     */
    public static HashMap<String, Integer> sortMap (HashMap<String, Integer> results)
    {
        LinkedHashMap<String, Integer> sortedmap = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> list = new LinkedList<>(results.entrySet());

        list.sort((m1, m2) -> {
            if (m1.getValue().equals(m2.getValue())) //Controllo se i valori sono uguali
                return m1.getKey().compareTo(m2.getKey()); //Compare le due chiavi perchè A deve essere
            else                                           //prima di B
                return m2.getValue().compareTo(m1.getValue()); //Compare i due valori perchè deve ritornare
        });
        List<Map.Entry<String, Integer>> first50element = list.stream().limit(50).collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : first50element) {
            sortedmap.put(entry.getKey(), entry.getValue());
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
