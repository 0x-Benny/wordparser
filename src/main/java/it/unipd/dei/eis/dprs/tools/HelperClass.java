package it.unipd.dei.eis.dprs.tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class HelperClass {
    public static HashSet<String> CreateStopList(String stoplistPath){
        HashSet<String> stoplist = new HashSet<>(); //Creo il set con le parole non volute
        try{
            List<String> allLines = Files.readAllLines(Paths.get("assets/utilities/english_stoplist_v1.txt"));
            stoplist.addAll(allLines);
        }catch(IOException e){ System.out.println("Stoplist file not found"); }
        stoplist.add("");
        return stoplist;
    }

    public static HashMap<String, Integer> sortMap (HashMap<String, Integer> results){
        ArrayList<Integer> list = new ArrayList<>();
        LinkedHashMap<String, Integer> sortedmap = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            list.add(entry.getValue());
        }

        Collections.sort(list, Collections.reverseOrder());
        ArrayList<Integer> first50element = (ArrayList<Integer>) list.stream().limit(50).collect(Collectors.toList());

        for (Integer num : first50element) {
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedmap.put(entry.getKey(), num);
                }
            }
        }
        return sortedmap;
    }

    public static void insertIntoMap(String[] completearticle, HashSet<String> stoplist, HashMap<String, Integer> results){
        for (String s : completearticle) { //Procedo per l'articolo completo
            if (!stoplist.contains(s)) { //Se la parole non è tra quelle brutte
                if (!results.containsKey(s) && s != null) { //e la mia hashmap non contiene già la parola
                    results.put(s, 1); //allora la inserisco e le do il valore 1
                } else if (results.containsKey(s) && s != null) { //se la contiene
                    results.put(s, results.get(s) + 1); //aumento il valore
                }
            }
        }
    }

    public static void toTXT(HashMap<String, Integer> results) throws IOException {
        String risultati = String.valueOf(results).replaceAll(",","\n");
        risultati = risultati.replaceAll("[^a-zA-Z0-9_+=+\\n]","");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("WordCounter.txt"), "UTF-8"));
        try {
            //out.write(String.valueOf(results));
            out.write(risultati);
        } finally {
            out.close();
        }

    }
}
