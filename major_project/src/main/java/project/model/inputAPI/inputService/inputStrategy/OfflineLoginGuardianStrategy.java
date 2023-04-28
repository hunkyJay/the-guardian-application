package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.inputAPI.inputService.InputAPIService;

/**
 * Offline strategy injected into login service
 */
public class OfflineLoginGuardianStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        String token = service.getToken();
        JsonObject loginResponse = null;
        String existedKey = System.getenv("INPUT_API_KEY");
        if("test".equals(token) || token.equals(existedKey)){
            String response = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":1}}";
            loginResponse = (JsonObject) JsonParser.parseString(response);
        }
        return loginResponse;
    }
}
