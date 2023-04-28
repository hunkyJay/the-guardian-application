package project.model.entity;

import java.util.List;

/**
 * The tag class with current matching contents
 * Serve as a record with attributes id, webUrl, and a matching content list
 */
public class Tag implements Entity {
    private String id;
    private String webUrl;
    private List<Content> matchingContents;

    public Tag(String id, String webUrl) {
        this.id = id;
        this.webUrl = webUrl;
    }

    public String getId() {
        return id;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public List<Content> getMatchingContents() {
        return matchingContents;
    }

    public void setMatchingContents(List<Content> matchingContents) {
        this.matchingContents = matchingContents;
    }

    @Override
    public String getEntityInfo() {
        String info = "Tag: " + id
                +"\nWeb Url: " + webUrl;
        return info;
    }
}
