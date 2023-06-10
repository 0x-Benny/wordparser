package it.unipd.dei.eis.dprs;

public class BasicArticle
{
    private String title;
    private String body;
    private String source;
    private String url;

    public BasicArticle() {}

    public BasicArticle(final String title, final String body, final String source, final String url)
    {
        this.title = title;
        this.body = body;
        this.source = source;
        this.url = url;
    }

    public String getTitle()
    {
        return title;
    }
    public String getBody()
    {
        return body;
    }
    public String getSource()
    {
        return source;
    }
    public String getUrl()
    {
        return url;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setBody(String body)
    {
        this.body = body;
    }
    public void setSource(String source)
    {
        this.source = source;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "[ARTICLE INFO] {\n" +
                "*SOURCE='" + getSource() + "'\n" +
                "*URL='" + getUrl() + "'\n" +
                "*TITLE='" + getTitle() + "'\n" +
                "*BODY='" + getBody() + "'\n" +
                '}';
    }
}