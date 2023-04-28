package project.model.outputAPI.outputService.outputStrategy;

import com.google.gson.JsonObject;
import project.model.outputAPI.outputService.OutputAPIService;
import project.model.util.HttpHelper;

/**
 *  Online strategy injected into output service
 */
public class OnlineOutputStrategy implements OutputServiceStrategy {
    @Override
    public JsonObject behave(OutputAPIService service) {
        String outputToken = service.getToken();
        String outputFile = service.getOutputContent();
        String authorization = "Client-ID " + outputToken;
        String uri = "https://api.imgur.com/3/image";
        //JsonObject outputResponse = HttpHelper.uploadFile(uri,outputFile, outputToken);
        JsonObject outputResponse = HttpHelper.postRequestWithAuthorization(uri, authorization, outputFile);

        return outputResponse;
    }
}
