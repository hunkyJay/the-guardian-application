package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.inputAPI.inputService.InputAPIService;
import project.model.inputAPI.inputService.UpdateCacheService;
import project.model.util.database.dao.ContentDao;
import project.model.util.database.dao.ContentDaoFactory;
import project.model.entity.Tag;

/**
 * Strategy injected into cache update service
 */
public class UpdateContentsCacheStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        ContentDao contentDao = ContentDaoFactory.create();
        Tag updatedTag = ((UpdateCacheService)service).getTag();
        contentDao.updateTagContents(updatedTag);

        String tagJson = new Gson().toJson(updatedTag);
        JsonObject result = (JsonObject) JsonParser.parseString(tagJson);

        return result;
    }
}
