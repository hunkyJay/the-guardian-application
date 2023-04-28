package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;
import project.model.util.database.dao.ContentDao;
import project.model.util.database.dao.ContentDaoFactory;
import project.model.inputAPI.inputService.InputAPIService;

/**
 *  The concrete strategy injected into Clear tag contents cache service
 */
public class ClearTagContentsCacheStrategy implements InputServiceStrategy {

    @Override
    public JsonObject behave(InputAPIService service) {
        ContentDao contentDao = ContentDaoFactory.create();
        contentDao.deleteTable();

        return null;
    }
}
