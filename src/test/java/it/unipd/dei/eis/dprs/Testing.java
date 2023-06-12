package it.unipd.dei.eis.dprs;

import it.unipd.dei.eis.dprs.tools.StrategyHelper;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Testing {
    private static final Random random = new Random();

    /*@Test
    public void CreateStopList() throws IOException{
        HashSet<String> stoplist = StrategyHelper.CreateStopList();
        assertFalse(stoplist.equals(null));
        assertFalse(stoplist.size() == 0);
    }
    */
    /**
     * Crea n parole random formate da 5 a 20 caratteri
     */
    private String[] randomStrings(int n){
        String[] randomStrings = new String[n];
        Random random = new Random();

        for(int i = 0; i < n; i++)
        {
            char[] word = new char[random.nextInt(15)+5]; // words of length 5 through 20
            for(int j = 0; j < word.length; j++)
            {
                word[j] = (char)('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }

    /**
     * Crea una mappa con 70 parole random e valori random + 4 valori per testare ogni caso.
     */
    private HashMap<String, Integer> createRandomMap(){
        Random random = new Random();
        HashMap<String, Integer> results = new HashMap<>();

        int n = 70;
        String[] randomStrings = randomStrings(n); //Metodo creato sopra

        for(int i = 0; i < 70; i++){
            results.put(randomStrings[i], random.nextInt(1000));
        }
        results.put("AAAAA", 1100);
        results.put("CCCCC", 1100);
        results.put("BBBBB", 1100);
        results.put("DDDDD", 1100);

        return results;
    }

    /**
     * Ritorna un array con le parole non contabili. ()
     */
    private String[] arrayBannedWords() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("assets/utilities/english_stoplist_v1.txt"));
        String stopwords;

        List<String> list = new ArrayList<>();
        while((stopwords = in.readLine()) != null){
            list.add(stopwords);
        }
        String[] tokens = new String[10];
        int count = 0;
        do{
            int index = random.nextInt(list.size());
            tokens[count] = list.get(index);
            count++;
        }while(count < 5);

        return tokens;
    }

    /**
     * Ritorna una lista con le parole non contabili.
     */
    private List<String> listBannedWords() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("assets/utilities/english_stoplist_v1.txt"));
        String stopwords;

        List<String> list = new ArrayList<>();
        while ((stopwords = in.readLine()) != null) {
            list.add(stopwords);
        }
        return list;
    }

    /**
     * Controllo che le parole vengano contate in modo giusto
     * (StrategyHelper - insertIntoMap)
     */
    @Test
    public void wrongCounting(){
        String[] randwords = randomStrings(5);

        String[] tokens = new String[7];
        System.arraycopy(randwords, 0, tokens, 0, 5);
        tokens[5] = tokens[0];
        tokens[6] = tokens[1];

        HashMap<String, Integer> results = new HashMap<>();

        StrategyHelper.insertIntoMap(tokens, results);

        assertFalse(results.get(tokens[0]) != 2 && results.get(tokens[1]) != 2);
        assertFalse(results.get(tokens[2]) != 1 && results.get(tokens[3]) != 1 && results.get(tokens[4]) != 1);
    }

    /**
     * Test per controllare che le parole "bannate" non vengano inserite nella mappa e le altre si
     * (StrategyHelper - insertIntoMap)
     */
    @Test
    public void bannedWordsInMap() throws IOException {
        HashMap<String, Integer> results = new HashMap<>();
        String[] tokens = arrayBannedWords();
        String[] randwords = randomStrings(5);

        System.arraycopy(randwords, 0, tokens, 5, 5);

        StrategyHelper.insertIntoMap(tokens, results);
        String[] wordinmap = results.keySet().toArray(new String[0]);

        List<String> list = listBannedWords();
        for(int i = 0; i < results.size(); i++){
            assertFalse(list.contains(wordinmap[i])); //Controllo che le parole nell hashmap non siano presenti
        }                                            //nella lista delle parole bannate
    }

    /**
     * Test per controllare che la mappa sia ordinata in modo corretto (Prima per value poi per Key).
     * (StrategyHelper - sortMap)
     */
    @Test
    public void sortingMap() {
        HashMap<String, Integer> results = createRandomMap();
        results = StrategyHelper.sortMap(results);

        Collection<Integer> values = results.values();
        Integer value[] = values.toArray(new Integer[0]);

        Collection<String> stringhe = results.keySet();
        String words[] = stringhe.toArray(new String[0]);

        for (int i = 0; i < value.length - 1; i++) {
            assertFalse("Test failed, value are not sorted in a decreasing way",
                    value[i] < value[i + 1]); //Controllo che i valori siano
            // in ordine decrescente

            //Controllo che in caso di valori uguali si segua l ordine alfabetico
            assertFalse("Test failed, words with same value aren't sorted following the alphabetical order",
                    value[i].equals(value[i + 1]) && words[i].compareTo(words[i + 1]) > 0);
        }
    }

    /**
     * Test per controllare la lunghezza della mappa dopo il sorting.
     * (StrategyHelper - sortMap)
     */
    @Test
    public void finalMapLength(){
        HashMap<String, Integer> results = createRandomMap();
        results = StrategyHelper.sortMap(results);

        assertFalse("Test failed cause the hashmap results is empty", results.isEmpty());
        assertFalse("Test failed cause the hashmap length is > 50",results.size() > 50);
    }

    /**
     * Test per controllare che la mappa venga correttamente trasportata su un file txt
     */
    @Test
    public void textFileDoesntexists() throws IOException {
        HashMap<String, Integer> results = createRandomMap();

        StrategyHelper.toTextfile(results);

        File file = new File("assets/wordCounter.txt");
        assertFalse("Test failed because wordCounter file doesn't exists or it is empty",
                !file.exists() || file.length() == 0);
    }
}