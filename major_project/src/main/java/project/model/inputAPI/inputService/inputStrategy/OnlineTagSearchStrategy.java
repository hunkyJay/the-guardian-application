package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;
import project.model.inputAPI.inputService.InputAPIService;
import project.model.util.HttpHelper;

/**
 * Online strategy injected into tag search service
 */
public class OnlineTagSearchStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        String token = service.getToken();
        String keyword = service.getKeyword();
        String url = "https://content.guardianapis.com/tags?web-title=" + keyword +"&api-key=" + token;
        JsonObject tagSearchResponse = HttpHelper.getRequest(url);

        return tagSearchResponse;
    }
}
