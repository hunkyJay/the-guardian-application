package project.model.outputAPI.outputService.outputStrategy;

import com.google.gson.JsonObject;
import project.model.outputAPI.outputService.OutputAPIService;

/**
 *  Offline strategy injected into output service
 */
public class OfflineOutputStrategy implements OutputServiceStrategy {
    @Override
    public JsonObject behave(OutputAPIService service) {
        return null;
    }
}
