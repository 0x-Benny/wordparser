package it.unipd.dei.eis.dprs;

import java.util.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import io.github.pepperkit.corenlp.stopwords.StopWordsAnnotator;

public class Wordparser
{
    public static ArrayList<String> filterWords(ArrayList<BasicArticle> a, String stoplistPath)
    {
        String text = a.get(0).getTitle().concat("\n").concat(a.get(0).getBody());
        for (int i = 1; i < a.size(); i++)
        {
            text = text + "\n" + a.get(i).getTitle().concat("\n").concat(a.get(i).getBody());
        }
        text = text.replaceAll("[^A-Za-z\\-\\s]+|(?<!\\b\\w{1,1})-(?!\\w{1,1}\\b)","");
        final Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, stopwords");
        props.setProperty("customAnnotatorClass.stopwords", "StopWordsAnnotator");
        props.setProperty("ssplit.isOneSentence", "true");
        props.setProperty("tokenize.options", "tokenizeNLs=false,splitAssimilations=false,splitHyphenated=false");
        // Filter out these words
        props.setProperty("stopwords.customListResourcesFilePath", stoplistPath);

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        ArrayList<String> result = new ArrayList<>();
        List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);

        for (CoreLabel token : tokens)
        {
            // token.get(StopWordsAnnotator.class) will be TRUE if the word is stopped
            if (!token.get(StopWordsAnnotator.class))
            {
                result.add(token.get(CoreAnnotations.LemmaAnnotation.class));
            }
        }
        return result;
    }
    // https://www.baeldung.com/java-count-duplicate-elements-arraylist
    public static TreeMap<String, Integer> wordCount(ArrayList<String> words)
    {
        TreeMap<String, Integer> frequency = new TreeMap<String, Integer>();
        for (String element : words)
        {
            if (frequency.containsKey(element))
            {
                frequency.put(element, frequency.get(element) + 1);
            }
            else
            {
                frequency.put(element, 1);
            }
        }
        return frequency;
    }
}
