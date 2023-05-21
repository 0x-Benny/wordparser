package it.unipd.dei.eis.dprs;

import com.apitheguardian.bean.Article;
import com.apitheguardian.bean.Response;
import com.apitheguardian.GuardianContentApi;
import com.mashape.unirest.http.exceptions.UnirestException;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class Wordparser
{
    public static void main( String[] args )
    {
        TheGuardianAdapter api = new TheGuardianAdapter("9b04d5bd-ae24-4ed7-8bde-7c57ee902f70");
        ResponseBody response = null;
        try {
            response = api.getContent();
        } catch (IOException e) {
            System.err.println("ERRORE.");
            throw new RuntimeException(e);
        }
        //Arrays.stream(response.getResults()).forEach(System.out::println);
        TGArticle test = new TGArticle();

    }
}
