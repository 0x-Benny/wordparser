package it.unipd.dei.eis.dprs;

import it.unipd.dei.eis.dprs.tools.HelperClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Testing {

    @Test
    public void CreateStopList() throws IOException{
        HashSet<String> stoplist = HelperClass.CreateStopList("assets/utilities/english_stoplist_v1.txt");
        assertFalse(stoplist.equals(null));
        assertFalse(stoplist.size() == 0);
    }


    private String[] randomStrings(){
        String[] randomStrings = new String[70];
        Random random = new Random();

        for(int i = 0; i < 50; i++)
        {
            char[] word = new char[random.nextInt(15)+5]; // words of length 5 through 18
            for(int j = 0; j < word.length; j++)
            {
                word[j] = (char)('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }

    @Test
    public void SortMap(){
        Random random = new Random();
        HashMap<String, Integer> results = new HashMap();

        String[] randomStrings = randomStrings(); //Metodo creato sopra

        for(int i = 0; i < 70; i++){
            results.put(randomStrings[i], random.nextInt(1000));
        }
        results.put("AAAAAA", 1100);
        results.put("Madonna", 1100);
        results.put("BBBBBB", 1100);
        results.put("Luca", 1100);

        assertFalse(results.size() == 50); //Controllo che prima di chiamare il metodo la dimensione sia
                                                    //diversa da 50

        results = HelperClass.sortMap(results);
        assertFalse(results.isEmpty()); //Controllo che ci sia effetivamente qualcosa
        assertFalse(results.size() != 50); //Controllo se m ha tagliato le parole in più

        Collection<Integer> values =  results.values();
        Integer valArray[] = values.toArray(new Integer[0]);

        Collection<String> stringhe = results.keySet();
        String Paroledure[] = stringhe.toArray(new String[0]);
        for(int i = 0; i < Paroledure.length -1; i++){
            System.out.println("In posizione: " + i + " VALORE: " + Paroledure[i]);
        } //Tocca implementare l ordine alfabetico

        for(int i = 0; i < valArray.length -1; i++){
            System.out.println("In posizione: " + i + " VALORE: " + valArray[i]);
            assertFalse(valArray[i] < valArray[i+1]); //Controllo che i valori siano
                                                              // in ordine decrescente
        }
    }
}
