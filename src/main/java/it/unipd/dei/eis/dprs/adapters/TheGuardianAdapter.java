package it.unipd.dei.eis.dprs.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.eis.dprs.BasicArticle;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TheGuardianAdapter implements ArticleManager
{
    private final static String TG_API_URL = "http://content.guardianapis.com/search";
    private final String apiKey;
    private final String query;
    public ArrayList<BasicArticle> articles = new ArrayList<>();

    public TheGuardianAdapter(final String apiKey, final String query)
    {
        this.apiKey = apiKey;
        this.query = query;
    }

    // Metodo ausiliario per formattare e pulire correttamente il testo HTML
    private String bodyFormatter(String text)
    {
        if(text != null)
        {
            return Jsoup.parse(text).wholeText().trim().replaceAll("[\n]{2,}", " ")
                .replaceAll("[ ]{2,}", " ").replaceAll("[ \n]{2,}", " ");
        }
        return null;
    }

    public BasicArticle[] fetchArticles()
    {
        // Costruzione della richiesta con libreria Okhttp
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = Objects.requireNonNull(HttpUrl.get(TG_API_URL)).newBuilder();
        queryUrlBuilder.addQueryParameter("api-key", apiKey);
        queryUrlBuilder.addQueryParameter("show-fields", "body");
        queryUrlBuilder.addQueryParameter("page-size", String.valueOf(200));
        if (query != null && !query.isEmpty())
        {
            queryUrlBuilder.addQueryParameter("q", query);
        }
        String url = queryUrlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Costruzione della risposta con libreria Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            JsonNode requestRoot = objectMapper.readTree(Objects.requireNonNull(client.newCall(request).execute().body()).string());
            JsonNode results = requestRoot.get("response").get("results");
            if (results != null && results.isArray())
            {
                for (JsonNode resultNode : results)
                {
                    String title = resultNode.get("webTitle").asText();
                    String body = bodyFormatter(resultNode.get("fields").get("body").asText());
                    articles.add(new BasicArticle(title, body, "TheGuardian"));
                    //System.out.println("**ARTICOLO CREATO**\nTITOLO: " + title + "\nTESTO: " + body + '\n');
                }
            }
            else
                System.err.println("++ERROR. TheGuardian's \"results\" field is null or invalid.");
        }
        catch (IOException e)
        {
            System.err.println("++ERROR. No response from TheGuardian's API. More details:");
            System.err.println(e.getMessage());
        }
        // Risultato finale
        return articles.toArray(new BasicArticle[0]);
    }
}
