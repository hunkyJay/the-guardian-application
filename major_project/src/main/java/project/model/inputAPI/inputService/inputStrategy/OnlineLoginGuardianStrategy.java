package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;;
import project.model.inputAPI.inputService.InputAPIService;
import project.model.util.HttpHelper;

/**
 * Online strategy injected into login service
 */
public class OnlineLoginGuardianStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        String token = service.getToken();
        String url = "https://content.guardianapis.com/?api-key=" + token;
        JsonObject loginResponse = HttpHelper.getRequest(url);

        return loginResponse;
    }
}
