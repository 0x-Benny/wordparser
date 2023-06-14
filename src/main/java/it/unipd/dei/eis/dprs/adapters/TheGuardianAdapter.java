package it.unipd.dei.eis.dprs.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.eis.dprs.BasicArticle;
import java.io.InputStream;
import java.util.Properties;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Gestisce l'accesso ad articoli del Guardian.
 * @see it.unipd.dei.eis.dprs.adapters.SourceAdapter
 */
public class TheGuardianAdapter implements SourceAdapter
{
    // URL dell'API del Guardian
    private final static String TG_API_URL = "http://content.guardianapis.com/search";
    // Chiave di accesso per l'API del Guardian
    private final String apiKey;
    // Termine di ricerca per l'API del Guardian
    private final String query;
    // Collezione di articoli
    public ArrayList<BasicArticle> articles = new ArrayList<>();

    public TheGuardianAdapter(final String query)
    {
        this.apiKey = keyLoader();
        this.query = query;
    }

    /**
     * Metodo statico ausiliario per prelevare la chiave privata di TheGuardianAPI dal file properties.
     * @return La chiave API.
     * @see java.util.Properties
     */
    private static String keyLoader()
    {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("guardian.properties");

        try
        {
            properties.load(input);
        }
        catch (IOException | IllegalArgumentException e)
        {
            System.err.println("++ERROR. Invalid input in properties file.");
            throw new RuntimeException(e);
        }
        return properties.getProperty("api-key");
    }

    /**
     * Metodo statico ausiliario per formattare e pulire correttamente il testo HTML.
     * @param text Testo da formattare.
     * @return Testo formattato.
     * @see org.jsoup.Jsoup
     */
    private static String bodyFormatter(String text)
    {
        if(text != null)
        {
            return Jsoup.parse(text).wholeText().trim().replaceAll("[\n]{2,}", " ")
                .replaceAll("[ ]{2,}", " ").replaceAll("[ \n]{2,}", " ");
        }
        return null;
    }

    /**
     * Metodo ausiliario per costruire la richiesta alle API di TheGuardian.
     * @return Richiesta da inviare.
     * @see okhttp3.HttpUrl
     */
    private Request requestBuilder()
    {
        HttpUrl.Builder queryUrlBuilder = Objects.requireNonNull(HttpUrl.get(TG_API_URL)).newBuilder();
        queryUrlBuilder.addQueryParameter("api-key", apiKey);
        queryUrlBuilder.addQueryParameter("show-fields", "body");
        queryUrlBuilder.addQueryParameter("page-size", String.valueOf(200));
        if (query != null && !query.isEmpty())
        {
            queryUrlBuilder.addQueryParameter("q", query);
        }
        String queryUrl = queryUrlBuilder.build().toString();
        return new Request.Builder()
            .url(queryUrl)
            .build();
    }

    /**
     * Costruisce una richiesta con Okhttp e preleva gli articoli ricevuti in formato JSON.
     * @return Array di articoli.
     * @see okhttp3.OkHttpClient
     * @see com.fasterxml.jackson.databind.ObjectMapper
     * @see com.fasterxml.jackson.databind.JsonNode
     */
    public BasicArticle[] fetchArticles()
    {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            JsonNode requestRoot = objectMapper.readTree(Objects.requireNonNull(client.newCall(requestBuilder()).execute().body()).string());
            JsonNode results = requestRoot.get("response").get("results");
            if (results != null && results.isArray() && !results.isEmpty())
            {
                for (JsonNode resultNode : results)
                {
                    String title = resultNode.get("webTitle").asText();
                    String body = bodyFormatter(resultNode.get("fields").get("body").asText());
                    String url = resultNode.get("webUrl").asText();
                    articles.add(new BasicArticle(title, body, "TheGuardian", url));
                }
            }
            else
                System.err.println("++ERROR. TheGuardian's \"results\" field is null, empty or invalid.");
        }
        catch (IOException | NullPointerException e)
        {
            System.err.println("++ERROR. No response from TheGuardian's API. More details: " + e.getMessage());
        }
        // Risultato finale
        return articles.toArray(new BasicArticle[0]);
    }
}