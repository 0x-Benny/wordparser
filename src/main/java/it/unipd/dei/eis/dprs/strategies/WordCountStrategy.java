package it.unipd.dei.eis.dprs.strategies;

import it.unipd.dei.eis.dprs.BasicArticle;

/**
 * Interfaccia che, in accordo con il pattern Strategy, definisce un metodo per il conteggio delle parole.
 */
public interface WordCountStrategy
{
    /**
     * Definisce una strategia di conteggio delle parole su un insieme di articoli.
     * @param articles Un array di articoli.
     */
    void wordCount(BasicArticle[] articles);
}