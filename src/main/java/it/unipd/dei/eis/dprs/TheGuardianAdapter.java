package it.unipd.dei.eis.dprs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TheGuardianAdapter {

    private final static String TG_API_URL = "http://content.guardianapis.com/search";
    private final String apiKey;
    public ArrayList<BasicArticle> articles = new ArrayList<BasicArticle>();

    public TheGuardianAdapter(final String apiKey) {
        this.apiKey = apiKey;
    }

    // Metodo ausiliario per formattare e pulire correttamente il testo HTML
    private String bodyFormatter(String text) {
        if(text != null) {
            return Jsoup.parse(text).wholeText().trim().replaceAll("[\n]{2,}", " ")
                .replaceAll("[ ]{2,}", " ").replaceAll("[ \n]{2,}", " ");
        }
        return null;
    }

    public BasicArticle[] fetchArticles() {
        return fetchArticles(null);
    }
    public BasicArticle[] fetchArticles(String query) {

        // Costruzione della richiesta con libreria Okhttp
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = Objects.requireNonNull(HttpUrl.get(TG_API_URL)).newBuilder();
        queryUrlBuilder.addQueryParameter("api-key", apiKey);
        queryUrlBuilder.addQueryParameter("show-fields", "body");
        if (query != null && !query.isEmpty()) {
            queryUrlBuilder.addQueryParameter("q", query);
        }
        String url = queryUrlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Costruzione della risposta con libreria Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode requestRoot = objectMapper.readTree(Objects.requireNonNull(client.newCall(request).execute().body()).string());

            JsonNode results = requestRoot.get("response").get("results");
            if (results != null && results.isArray()) {
                for (JsonNode resultNode : results) {
                    String title = resultNode.get("webTitle").asText();
                    String body = bodyFormatter(resultNode.get("fields").get("body").asText());
                    articles.add(new BasicArticle(title, body, "TheGuardian"));
                    System.out.println("**ARTICOLO CREATO**\nTITOLO: " + title + "\nTESTO: " + body + '\n');
                }
            }
        } catch (IOException e) {
            System.out.println("Errore.");
            e.printStackTrace();
        }

        // Risultato finale
        return articles.toArray(new BasicArticle[0]);
    }
}
