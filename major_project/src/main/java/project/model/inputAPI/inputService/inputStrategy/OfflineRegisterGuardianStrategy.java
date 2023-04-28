package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.inputAPI.inputService.InputAPIService;

/**
 * Offline strategy injected into register service
 */
public class OfflineRegisterGuardianStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        String response = "{\"response\":" +
                "{\"information\":\"Successfully created the test key: test \nOr go to the website to create online.\"," +
                "\"link\":\"https://open-platform.theguardian.com/access/\"}}";
        JsonObject registerResponse = (JsonObject) JsonParser.parseString(response);
        return registerResponse;
    }
}
