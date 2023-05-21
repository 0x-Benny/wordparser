package it.unipd.dei.eis.dprs;

public class TGArticle {
    private String id;
    private String type;
    private String sectionId;
    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;
    private String apiUrl;
    private String[] fields;
    private boolean isHosted;
    private String pillarId;
    private String pillarName;

    public TGArticle() {
    }

    public TGArticle(final String id, final String type, final String sectionId,
                     final String sectionName, final String webPublicationDate, final String webTitle,
                     final String webUrl, final String apiUrl, final String[] fields,
                     final boolean isHosted, final String pillarId, final String pillarName) {
        this.id = id;
        this.type = type;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.apiUrl = apiUrl;
        this.fields = fields;
        this.isHosted = isHosted;
        this.pillarId = pillarId;
        this.pillarName = pillarName;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String[] getFields() {
        return fields;
    }

    public boolean getIsHosted() {
        return isHosted;
    }

    public String getPillarId() {
        return pillarId;
    }

    public String getPillarName() {
        return pillarName;
    }

    @Override
    public String toString() {
        return "TheGuardian Article info {\n" +
                "id='" + id + "'\n" +
                ", type='" + type + "'\n" +
                ", sectionId='" + sectionId + "'\n" +
                ", sectionName='" + sectionName + "'\n" +
                ", webPublicationDate='" + webPublicationDate + "'\n" +
                ", webTitle='" + webTitle + "'\n" +
                ", webUrl='" + webUrl + "'\n" +
                ", apiUrl='" + apiUrl + "'\n" +
                ", body='" + fields[0] + "'\n" +
                ", isHosted='" + isHosted + "'\n" +
                ", pillarId='" + pillarId + "'\n" +
                ", pillarName='" + pillarName + "'\n" +
                '}';
    }
}

