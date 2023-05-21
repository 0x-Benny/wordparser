package it.unipd.dei.eis.dprs;

public class Wordparser
{
    public static void main( String[] args )
    {
        TheGuardianAdapter api = new TheGuardianAdapter("9b04d5bd-ae24-4ed7-8bde-7c57ee902f70");
        api.fetchArticles();
    }
}
