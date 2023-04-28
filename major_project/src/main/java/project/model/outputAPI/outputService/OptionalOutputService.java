package project.model.outputAPI.outputService;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import project.model.entity.Entity;
import project.model.entity.Output;
import project.model.outputAPI.outputService.outputStrategy.OutputServiceStrategy;

/**
 * The service class used to send results to the optional output api
 */
public class OptionalOutputService implements OutputAPIService {
    private String username;
    private String accessToken;
    private String outputReport;
    private OutputServiceStrategy optionalOutputStrategy;

    public OptionalOutputService(String username, String accessToken, String outputReport, OutputServiceStrategy optionalOutputStrategy) {
        this.username = username;
        this.accessToken = accessToken;
        this.outputReport = outputReport;
        this.optionalOutputStrategy = optionalOutputStrategy;
    }

    /**
     * send results to the optional output api
     * @return Entity return the Output entity
     */
    @Override
    public Entity operate() {
        Output output = null;
        JsonObject response = optionalOutputStrategy.behave(this);
        if(response != null && response.get("success") != null){
            if(response.get("success").getAsBoolean()) {
                JsonArray jquery = response.get("jquery").getAsJsonArray();
                JsonArray link = jquery.get(10).getAsJsonArray().get(3).getAsJsonArray();
                String linkStr = link.get(0).getAsString();
                output = new Output(null, "optional output", null,linkStr);
            }
        }
        return output;
    }

    /**
     * Get the username for this optional output service
     * @return The username String
     */
    public String getUsername(){
        return username;
    }


    /**
     * Get the access token for this optional output service
     * @return The access token String
     */
    @Override
    public String getToken() {
        return accessToken;
    }

    /**
     * Get the result to output
     * @return The result as a form of String to output
     */
    @Override
    public String getOutputContent() {
        return outputReport;
    }
}
