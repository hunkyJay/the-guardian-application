package project.model.inputAPI.inputService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import project.model.entity.Entity;
import project.model.entity.Tag;
import project.model.inputAPI.inputService.inputStrategy.InputServiceStrategy;

/**
 * The service class used for searching data from the existing cache
 */
public class SearchCacheService implements InputAPIService {
    private InputServiceStrategy searchCacheStrategy;
    private String tagId;

    public SearchCacheService(InputServiceStrategy searchCacheStrategy, String tagId) {
        this.searchCacheStrategy = searchCacheStrategy;
        this.tagId = tagId;
    }

    /**
     * Search tag from cache
     * @return Entity return tag entity searched
     */
    @Override
    public Entity operate() {
        Tag tagQueried = null;
        JsonObject tagObject = searchCacheStrategy.behave(this);
        Tag tag = new Gson().fromJson(tagObject, Tag.class);
        if(tag.getWebUrl() !=null ){
            tagQueried = tag;
        }
        return tagQueried;
    }

    @Override
    public String getToken() {
        //Currently, storing user token is not required
        return null;
    }

    /**
     * Get the tag id used to query in the cache
     * @return The tag id String
     */
    @Override
    public String getKeyword() {
        return tagId;
    }
}
