package project.model.entity;

import java.util.List;

/**
 * The Content search keyword with a list of related contents
 * Serve as a record with attributes keyword and a relatedContent list
 */
public class ContentSearch implements Entity{
    private String keyword;
    private List<Content> relatedContents;

    public ContentSearch(String keyword, List<Content> relatedContents) {
        this.keyword = keyword;
        this.relatedContents = relatedContents;
    }

    public String getKeyword() {
        return keyword;
    }

    public List<Content> getRelatedContents() {
        return relatedContents;
    }

    @Override
    public String getEntityInfo() {
        return "Your contents searching keywords: " + keyword;
    }
}
