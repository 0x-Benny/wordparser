package it.unipd.dei.eis.dprs;

import it.unipd.dei.eis.dprs.strategies.ArticleOccurrencesCountStrategy;
import it.unipd.dei.eis.dprs.strategies.TotalOccurrencesCountStrategy;
import it.unipd.dei.eis.dprs.strategies.WordCountStrategy;
import it.unipd.dei.eis.dprs.tools.Serializer;
import it.unipd.dei.eis.dprs.tools.StrategyHelper;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Testing {
    private static final Random random = new Random();

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
     * Crea una mappa con n parole e valori random (per testare che funzioni con più e meno di 50 parole)
     * + 4 valori per testare ogni caso.
     */
    private HashMap<String, Integer> createRandomMap(int n){
        Random random = new Random();
        HashMap<String, Integer> results = new HashMap<>();

        String[] randomStrings = randomStrings(n); //Metodo creato sopra

        for(int i = 0; i < n; i++){
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
     * Ritorna una lista con parole non contabili scelte casualmente
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
     * Test per controllare che le parole non contabili non vengano inserite nella mappa e le altre si
     * {@link StrategyHelper#insertIntoMap(String[], HashMap)}
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
        for(int i = 0; i < results.size(); i++)
            assertFalse(list.contains(wordinmap[i]), "Test failed, list contains not permitted word");
    }

    /**
     * Test per controllare l'effettiva esistenza della mappa dopo il sorting.
     * {@link StrategyHelper#sortMap(HashMap)}
     */
    @Test
    public void finalMapLength(){
        int n = random.nextInt(100) + 1;
        HashMap<String, Integer> results = createRandomMap(n);
        results = StrategyHelper.sortMap(results);

        assertFalse(results.isEmpty(), "Test failed cause the hashmap results is empty");
            assertFalse(results.size() > 50, "Test failed cause the hashmap length is > 50");
    }

    /**
     * Test per controllare che la mappa sia ordinata in modo corretto (Prima per value poi per Key).
     * {@link StrategyHelper#sortMap(HashMap)}
     */
    @Test
    public void sortingMap() {
        int n = random.nextInt(101);
        HashMap<String, Integer> results = createRandomMap(n);
        results = StrategyHelper.sortMap(results);

        Collection<Integer> values = results.values();
        Integer value[] = values.toArray(new Integer[0]);

        Collection<String> stringhe = results.keySet();
        String words[] = stringhe.toArray(new String[0]);

        for (int i = 0; i < value.length - 1; i++) {
            assertFalse(value[i] < value[i + 1],
                    "Test failed, value are not sorted in a decreasing way"); //Controllo che i valori siano
                                                       // in ordine decrescente

            //Controllo che in caso di valori uguali si segua l ordine alfabetico
            assertFalse(value[i].equals(value[i + 1]) && words[i].compareTo(words[i + 1]) > 0,
                    "Test failed, words with same value aren't sorted following the alphabetical order");
        }
    }

    /**
     * Test per controllare che la mappa venga correttamente trasportata su un file txt
     * {@link StrategyHelper#toTextfile(HashMap)}
     */
    @Test
    public void textFileDoesntexists() throws IOException {
        int n = random.nextInt(101);
        HashMap<String, Integer> results = createRandomMap(n);

        StrategyHelper.toTextfile(results);

        File file = new File("assets/wordCounter.txt");
        assertFalse(!file.exists() || file.length() == 0,
                "Test failed because wordCounter file doesn't exists or it is empty");
    }

    /**
     * Scrive sul database un articolo generato casualmente (avendo però 2 parole ripetute)testando i 2 metodi
     * di conta, per ArticleOccurencesCountStrategy {@link ArticleOccurrencesCountStrategy} mi aspetto che vengano
     * contate 1 singola volta anche le parole ripetute.  Per TotalOccurencesCountStrategy {@link TotalOccurrencesCountStrategy}
     * mi aspetto che vengano contate 2 volte le arole ripetute
     */
    @Test
    public void strategyTest() throws IOException {
        String[] randomwords = randomStrings(5);

        String[] tokens = new String[7];
        System.arraycopy(randomwords, 0, tokens, 0, 5);
        tokens[5] = tokens[0];
        tokens[6] = tokens[1];

        String title = tokens[0] + " " + tokens[1] + " " + tokens[2];
        String body = tokens[3] + " " + tokens[4] + " " + tokens[5] + " " + tokens[6];

        BasicArticle article = new BasicArticle(title, body, "lercio", "www.prova.it");
        BasicArticle[] articles = new BasicArticle[1];
        articles[0] = article;

        Serializer.serialize(articles);

        WordCountStrategy strategy = new ArticleOccurrencesCountStrategy();
        Database.analyze(strategy);

        BufferedReader in = new BufferedReader(new FileReader("assets/wordCounter.txt"));
        String words;
        int[] value = new int[7];

        int index = 0;
        while((words = in.readLine()) != null){
            words = words.replaceAll("[^0-9]","");
            value[index] = Integer.parseInt(words);
            index++;
        }

        assertFalse(value[0] != 1 || value[1] != 1,
                "Test failed, wrong counting for ArticleOccurrencesCountStrategy()");

        in = new BufferedReader(new FileReader("assets/wordCounter.txt"));
        strategy = new TotalOccurrencesCountStrategy();
        Database.analyze(strategy);

        index = 0;
        while((words = in.readLine()) != null){
            words = words.replaceAll("[^0-9]","");
            value[index] = Integer.parseInt(words);
            index++;
        }
        assertFalse(value[0] != 2 || value[1] != 2,
                "Test failed, wrong counting for TotalOccurencesContStrategy()");
    }
}