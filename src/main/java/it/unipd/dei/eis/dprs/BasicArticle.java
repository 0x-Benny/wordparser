package it.unipd.dei.eis.dprs;

public class BasicArticle {
    private String source;
    private String title;
    private String body;

    public BasicArticle() {
    }

    public BasicArticle(final String title, final String body, final String source) {
        this.source = source;
        this.title = title;
        this.body = body;
    }

    public String getSource() {
        return source;
    }
    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "++[Informazioni Articolo]++ {\n" +
                ", *SORGENTE='" + source + "'\n" +
                ", *TITOLO='" + title + "'\n" +
                ", *TESTO='" + body + "'\n" +
                '}';
    }
}
