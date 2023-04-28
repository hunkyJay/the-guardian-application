package project.model.entity;

/**
 * The Short form result of the searching engine a selected tag with a selected content
 * Serve as a record with attributes selected tag, and selected content
 */
public class ShortFormResult implements Entity{
    private Tag selectedTag;
    private Content selectedContent;

    public ShortFormResult(Tag selectedTag, Content selectedContent) {
        this.selectedTag = selectedTag;
        this.selectedContent = selectedContent;
    }

    public Tag getSelectedTag() {
        return selectedTag;
    }

    public Content getSelectedContent() {
        return selectedContent;
    }

    @Override
    public String getEntityInfo() {
        String info = "Tag: " + selectedTag.getId() + "  "
                + "\nContent: " + selectedContent.getWebTitle() + "  "
                + selectedContent.getWebUrl();

        return info;
    }
}
