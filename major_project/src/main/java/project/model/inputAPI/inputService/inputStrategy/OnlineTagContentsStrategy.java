package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;
import project.model.inputAPI.inputService.InputAPIService;
import project.model.util.HttpHelper;

/**
 * Online strategy injected into Tag Contents getting service
 */
public class OnlineTagContentsStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        String token = service.getToken();
        String tag = service.getKeyword();
        String url = "https://content.guardianapis.com/" + tag + "?page-size=100&api-key=" + token;
        JsonObject tagContentsResponse = HttpHelper.getRequest(url);

        return tagContentsResponse;
    }
}
