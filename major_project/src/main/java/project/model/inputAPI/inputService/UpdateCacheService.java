package project.model.inputAPI.inputService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import project.model.entity.Entity;
import project.model.entity.Tag;
import project.model.inputAPI.inputService.inputStrategy.InputServiceStrategy;

/**
 * The service class used for updating cache data
 */
public class UpdateCacheService implements InputAPIService {
    private InputServiceStrategy updateCacheStrategy;
    private Tag tag;

    public UpdateCacheService(InputServiceStrategy updateCacheStrategy, Tag tag) {
        this.updateCacheStrategy = updateCacheStrategy;
        this.tag = tag;
    }

    /**
     * Update the tag cache
     * @return Entity return the updated tag in cache
     */
    @Override
    public Entity operate() {
        JsonObject tagObject = updateCacheStrategy.behave(this);
        Tag tag = new Gson().fromJson(tagObject, Tag.class);

        return tag;
    }

    @Override
    public String getToken() {
        //Currently, storing user token is not required
        return null;
    }

    @Override
    public String getKeyword() {
        return tag.getId();
    }

    public Tag getTag() {
        return tag;
    }
}
