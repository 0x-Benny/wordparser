package it.unipd.dei.eis.dprs;

public class BasicArticle
{
    private String title;
    private String body;
    private String source;

    public BasicArticle() {}

    public BasicArticle(final String title, final String body, final String source)
    {
        this.source = source;
        this.title = title;
        this.body = body;
    }

    public String getSource()
    {
        return source;
    }
    public String getTitle()
    {
        return title;
    }
    public String getBody()
    {
        return body;
    }

    public void setSource(String source)
    {
        this.source = source;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setBody(String body)
    {
        this.body = body;
    }

    @Override
    public String toString()
    {
        return "[ARTICLE INFO] {\n" +
                "*SOURCE='" + getSource() + "'\n" +
                "*TITLE='" + getTitle() + "'\n" +
                "*BODY='" + getBody() + "'\n" +
                '}';
    }
}

