package it.unipd.dei.eis.dprs.adapters;

import it.unipd.dei.eis.dprs.BasicArticle;

public interface SourceAdapter
{
    BasicArticle[] fetchArticles();
}