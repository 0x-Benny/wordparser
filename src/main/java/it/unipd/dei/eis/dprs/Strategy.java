package it.unipd.dei.eis.dprs;

import java.util.HashMap;

public interface Strategy {
    public HashMap<String, Integer> wordCount(BasicArticle[] basicarticle, String stoplistPath);
}
