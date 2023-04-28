package project.model.outputAPI.outputService.outputStrategy;

import com.google.gson.JsonObject;
import project.model.outputAPI.outputService.OutputAPIService;

/**
 * Strategy injected into output services.
 */
public interface OutputServiceStrategy {
    /**
     * Strategy behaviour for the service
     * @param service The output api service for the strategy to inject
     * @return the result as json object
     */
    JsonObject behave(OutputAPIService service);
}
