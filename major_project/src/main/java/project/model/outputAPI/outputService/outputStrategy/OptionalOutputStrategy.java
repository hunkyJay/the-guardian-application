package project.model.outputAPI.outputService.outputStrategy;

import com.google.gson.JsonObject;
import project.model.outputAPI.outputService.OutputAPIService;
import project.model.outputAPI.outputService.OptionalOutputService;
import project.model.util.HttpHelper;

/**
 *  Online strategy injected into optional output service
 */
public class OptionalOutputStrategy implements OutputServiceStrategy {
    @Override
    public JsonObject behave(OutputAPIService service) {
        String uri = "https://oauth.reddit.com/api/submit";
        String username = ((OptionalOutputService)service).getUsername();
        String report = service.getOutputContent();
        String accessToken = service.getToken();
        String authorization = "bearer " + accessToken;
        String content ="sr=u_" + username + "&title=Short Form Result&kind=self&text=" + report;

        JsonObject result = HttpHelper.postRequestWithAuthorization(uri,authorization, content);

        return result;
    }
}
