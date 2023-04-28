package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.util.database.dao.ContentDao;
import project.model.util.database.dao.ContentDaoFactory;
import project.model.entity.Tag;
import project.model.inputAPI.inputService.InputAPIService;

/**
 * Strategy injected into cache search service
 */
public class SearchContentsCacheStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        ContentDao contentDao = ContentDaoFactory.create();
        Tag tagQueried = contentDao.queryContentsByTagId(service.getKeyword());
        String tagJson = new Gson().toJson(tagQueried);
        JsonObject result = (JsonObject) JsonParser.parseString(tagJson);

        return result;
    }
}
