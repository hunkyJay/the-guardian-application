package project.model.inputAPI.facade;

import project.model.entity.Entity;
import project.model.entity.LongFormResult;
import project.model.entity.ShortFormResult;
import project.model.entity.Tag;

/**
 * Facade interface for the input api
 */
public interface InputAPI {
    /**
     * Login function for the logical layer, if successful return a logged-in user
     *
     * @return An Entity object (user)
     */
    Entity login(String token);

    /**
     * Register function for the logical layer, return a RegisterInfo object
     *
     * @return An Entity object (RegisterInfo)
     */
    Entity register();

    /**
     * @param token The user's token
     * @param tagKeyword The input Keyword to search related tags
     * @return An Entity object (TagSearch) with a list of Tags
     */
    Entity searchRelatedTags(String token, String tagKeyword);

    /**
     * @param token The user's token
     * @param tag The selected tag to search related contents
     * @return An Entity object (Tag) with a list of Contents
     */
    Entity getTagContents(String token, String tag);

    /**
     * @param tag The selected tag to search related contents
     * @return An Entity object (Tag) with a list of Contents from the cache
     */
    Entity getTagContentsCache(String tag);

    /**
     * @param token The user's token
     * @param keyword The keywords related to content
     * @return An Entity object (ContentSearch) with a list of Contents
     */
    Entity searchContents(String token, String keyword);

    /**
     * @param tag The  tag with related contents to update
     * @return An Entity object (Tag) with a list of Contents from the cache
     */
    Entity updateTagContentsCache(Tag tag);

    /**
     * Clear cache for the input api
     * @return
     */
    Entity clearCache();

    /**
     * The long form result to report
     *
     * @return An Entity object (LongFormResult)
     */
    Entity getLongFormResult();

    /**
     * The short form result to report
     *
     * @return An Entity object (ShortFormResult)
     */
    Entity getShortFormResult();

    /**
     * Set LongFormResult for the api
     */
    void setLongFormResult(LongFormResult result);

    /**
     * Set ShortFormResult for the api
     */
    void setShortFormResult(ShortFormResult result);

    /**
     * The current token used for api functions
     *
     * @return A token String
     */
    String getCurrentToken();

    /**
     * Set token used for the api
     */
    void setCurrentToken(String token);

    /**
     * Check whether the api is online mode
     *
     * @return true online, false offline
     */
    boolean isOnline();

    /**
     * Create a database for the api from the DBConnection instance
     */
    void createDatabase();

    /**
     * Set the current tag credits
     * @param credits the value to set
     */
    void setCurrentTagCredits(int credits);

    /**
     * Get the current tag credits
     * @return the value of current tag credits
     */
    int getCurrentTagCredits();

    /**
     * Get the current rule used for the credit initializing and consuming
     * @return An Entity object (SearchCreditRule)
     */
    Entity currentCreditRule();

    /**
     * Consume the search credits once
     * @return true consume successfully, false failed.
     */
    boolean consumeCredit();

}
