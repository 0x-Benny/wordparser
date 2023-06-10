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

public class StrategyHelper
{
    private static HashSet<String> createStopList()
    {
        HashSet<String> stoplist = new HashSet<>(); //Creo il set con le parole non volute
        try
        {
            List<String> allLines = Files.readAllLines(Paths.get("./assets/utilities/english_stoplist_v1.txt"));
            stoplist.addAll(allLines);
            stoplist.add("");
        }
        catch(IOException e)
        {
            System.err.println("++ERROR. Stoplist file not found.");
        }
        return stoplist;
    }

    public static void insertIntoMap(String[] tokens, HashMap<String, Integer> results)
    {
        HashSet<String> stoplist = StrategyHelper.createStopList();
        for (String word : tokens)
        { //Procedo per l'articolo completo
            if (!stoplist.contains(word))
            { //Se la parole non è tra quelle brutte
                if (!results.containsKey(word) && word != null)
                { //e la mia hashmap non contiene già la parola
                    results.put(word, 1); //allora la inserisco e le do il valore 1
                }
                else if (results.containsKey(word) && word != null)
                { //se la contiene
                    results.put(word, results.get(word) + 1); //aumento il valore
                }
            }
        }
    }

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

    public static void toTextfile(HashMap<String, Integer> results) throws IOException // Trasforma in un file .txt la mappa
    {
        String result = String.valueOf(results).replaceAll(",","\n").replaceAll("[^a-zA-Z0-9_+=+\\n]","");
        if (result.equals(""))
        {
            System.err.println("++WARNING. Database is empty.");
        }
        try (Writer out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get("./assets/WordCounter.txt")), StandardCharsets.UTF_8)))
        {
            out.write(result);
        }
    }
}
