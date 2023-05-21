package it.unipd.dei.eis.dprs;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TheGuardianAdapter {

    /* ** Robaccia da valutare **
    static {
    // Only one time
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    */

    private final static String TG_API_URL = "http://content.guardianapis.com/search";
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String apiKey;
    private String section;
    private String tag;
    private Date toDate;
    private Date fromDate;

    public TheGuardianAdapter(final String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setFromDate(Date date) {
        this.fromDate = date;
    }

    public void setToDate(Date date) {
        this.toDate = date;
    }
            //TGREsponse as return type
    public ResponseBody getContent() throws IOException { //throws UnirestException {
        return getContent(null);
    }
            //TGREsponse as return type
    public ResponseBody getContent(String query) throws IOException { //,UnirestException


        ObjectMapper objectMapper = new ObjectMapper();
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder queryUrlBuilder = Objects.requireNonNull(HttpUrl.get(TG_API_URL)).newBuilder();
        queryUrlBuilder.addQueryParameter("api-key", apiKey);
        queryUrlBuilder.addQueryParameter("show-fields", "body");

        String url = queryUrlBuilder.build().toString();
        //JSONObject jsonObject = new JSONObject(); //c'è da creare un oggetto json
        //RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                //.post(body)
                .build();

        try {
            //Response response = client.newCall(request).execute();
            TGWrapper entity = objectMapper.readValue(client.newCall(request).execute().body().string(), TGWrapper.class);
            //System.out.println(entity);
            //TGArticle[] test = new TGArticle[entity.getResponse().getTotal()];
            //test = entity.getResponse().getResults();
            //System.out.println(test);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //return response.body(); .string()
        /* ** Altra robaccia da valutare **
        HttpRequest request = Unirest.get(TG_API_URL)
                .queryString("api-key", apiKey)
                .header("accept", "application/json");
        if (query != null && !query.isEmpty()) {
            request.queryString("q", query);
        }

        if (section != null && !section.isEmpty()) {
            request.queryString("section", section);
        }

        if (tag != null && !tag.isEmpty()) {
            request.queryString("tag", tag);
        }

        if (fromDate != null){
            request.queryString("from-date", dateFormat.format(fromDate));
        }

        if (toDate != null){
            request.queryString("to-date", dateFormat.format(toDate));
        }

        HttpResponse<ResponseWrapper> response = request.asObject(ResponseWrapper.class);
        return response.getBody().getResponse();
        */
    }
}
