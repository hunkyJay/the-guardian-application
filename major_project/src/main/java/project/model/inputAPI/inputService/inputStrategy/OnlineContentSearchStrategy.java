package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;
import project.model.inputAPI.inputService.InputAPIService;
import project.model.util.HttpHelper;

/**
 *  Online strategy injected into Content Search service
 */
public class OnlineContentSearchStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        String token = service.getToken();
        String keyword = service.getKeyword();
        String url = "https://content.guardianapis.com/search?" + keyword + "&page-size=50&api-key=" + token;
        JsonObject contentSearchResponse = HttpHelper.getRequest(url);

        return contentSearchResponse;
    }
}
