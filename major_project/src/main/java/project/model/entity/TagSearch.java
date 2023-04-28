package project.model.entity;

import java.util.List;

/**
 * The Tag search keyword with a list of related tags
 * Serve as a record with tag keyword, and a related tag list
 */
public class TagSearch implements Entity{
    private String keyword;
    private List<Tag> relatedTags;

    public TagSearch(String keyword, List<Tag> relatedTags) {
        this.keyword = keyword;
        this.relatedTags = relatedTags;
    }

    public String getKeyword() {
        return keyword;
    }

    public List<Tag> getRelatedTags() {
        return relatedTags;
    }

    @Override
    public String getEntityInfo() {
        return "Your tags searching keywords: " + keyword;
    }
}
