package it.unipd.dei.eis.dprs.adapters;

import it.unipd.dei.eis.dprs.BasicArticle;

/**
 * Gestisce l'accesso ad articoli di testate diverse applicando il pattern Adapter.
 */
public interface SourceAdapter
{
    /**
     * Preleva gli articoli.
     * @return Array di articoli.
     */
    BasicArticle[] fetchArticles();
}
