package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;
import project.model.inputAPI.inputService.InputAPIService;

/**
 * Strategy injected into input services.
 */
public interface InputServiceStrategy {
    /**
     * Strategy behaviour for the input service
     * @param service The input api service for the strategy to inject
     */
    JsonObject behave(InputAPIService service);
}
