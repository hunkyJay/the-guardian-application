package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.inputAPI.inputService.InputAPIService;

/**
 * Online strategy injected into register service
 */
public class OnlineRegisterGuardianStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        String response = "{\"response\":" +
                "{\"information\":\"Please go to the register website to create new key\"," +
                "\"link\":\"https://open-platform.theguardian.com/access/\"}}";
        JsonObject registerResponse = (JsonObject) JsonParser.parseString(response);
        return registerResponse;
    }
}
