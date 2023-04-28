package project.model.entity;

import java.util.List;

/**
 * The Long form result of the searching engine: a selected tag with a list of related contents
 * Serve as a record with attributes selectedTag and a related content list
 */
public class LongFormResult implements Entity{
    private Tag selectedTag;
    private List<Content> contentList;

    public LongFormResult(Tag selectedTag, List<Content> contentList) {
        this.selectedTag = selectedTag;
        this.contentList = contentList;
    }

    public Tag getSelectedTag() {
        return selectedTag;
    }

    public List<Content> getContentList() {
        return contentList;
    }

    @Override
    public String getEntityInfo() {
        StringBuilder sb = new StringBuilder(selectedTag.getEntityInfo());
        for(Content t: contentList){
            sb.append(t.getWebTitle());
            sb.append(" \n");
        }
        String info = sb.toString();

        return info;
    }
}
