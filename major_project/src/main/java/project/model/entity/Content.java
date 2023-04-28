package project.model.entity;

/**
 * The Content class for single content of the searching engine
 * Serve as a record with attributes id, webTitle,and webPublicationDate
 */
public class Content implements Entity{
    private String id;
    private String webTitle;
    private String webPublicationDate;
    private String webUrl;

    public Content(String id, String webTitle, String webPublicationDate, String webUrl) {
        this.id = id;
        this.webTitle = webTitle;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
    }

    public String getId() {
        return id;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    @Override
    public String getEntityInfo() {
        String info = "Web Tile: " + webTitle + "\n" +
                "Web Publication Date: " + webPublicationDate + "\n" +
                "Web Url: " + webUrl + "\n";
        return info;
    }
}
