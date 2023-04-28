package project.model.util.database.dao;

import project.model.entity.Tag;

/**
 * The interface of Database Access Objects to perform CURD operations for contents table in database
 */
public interface ContentDao {

    /**
     * Create content table in the database
     */
    void createTable();

    /**
     * Add contents related to a tag to the content table
     * @param tag The tag with its related contents
     */
    void addTagContents(Tag tag);

    /**
     * Query contents related to one tag
     * @param tagId The id of the tag
     * @return Tag Return a tag with its related contents
     */
    Tag queryContentsByTagId(String tagId);

    /**
     * Update contents related to a tag in the content table
     * @param tag The tag with its related contents
     */
    void updateTagContents(Tag tag);

    /**
     * Delete content table in the database
     */
    void deleteTable();
}
