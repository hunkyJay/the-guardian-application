package project.model.outputAPI.outputService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import project.model.entity.Entity;
import project.model.entity.Output;
import project.model.outputAPI.outputService.outputStrategy.OutputServiceStrategy;

/**
 * The service class used to send results to the output api
 */
public class OutputService implements OutputAPIService {
    private String clientID;
    private String outputFile;
    private OutputServiceStrategy outputStrategy;

    public OutputService(OutputServiceStrategy outputStrategy, String clientID, String outputFile) {
        this.clientID = clientID;
        this.outputFile = outputFile;
        this.outputStrategy = outputStrategy;
    }

    /**
     * send results to the output api
     * @return Entity return the Output entity
     */
    @Override
    public Entity operate() {
        JsonObject outputResponse = outputStrategy.behave(this);
        Output output = null;

        if(outputResponse != null) {
            //User gson to convert json object to output object
            Gson gson = new Gson();
            JsonObject outputObject = outputResponse.get("data").getAsJsonObject();
//            System.out.println(outputObject);//
            output = gson.fromJson(outputObject, Output.class);
        }

        return output;
    }


    /**
     * Get the client id for this service
     * @return The client id String
     */
    @Override
    public String getToken() {
        return clientID;
    }


    /**
     * Get the result to output
     * @return The result as a form of String to output
     */
    @Override
    public String getOutputContent() {
        return outputFile;
    }
}
