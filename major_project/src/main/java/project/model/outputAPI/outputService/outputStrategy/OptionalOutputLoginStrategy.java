package project.model.outputAPI.outputService.outputStrategy;

import com.google.gson.JsonObject;
import project.model.outputAPI.outputService.OptionalOutputLoginService;
import project.model.outputAPI.outputService.OutputAPIService;
import project.model.util.HttpHelper;

import java.util.Base64;

/**
 *  Online strategy injected into optional output login service
 */
public class OptionalOutputLoginStrategy implements OutputServiceStrategy {

    @Override
    public JsonObject behave(OutputAPIService service) {
//        String clientId = "SFr0YSCZDqQ3nvaUBHLp_Q";
//        String secret = "pE6Pq-LJ-WFAjMe3VvVl9U14QCgMJw";

        //Get values from the environment variables
        String clientId = System.getenv("REDDIT_API_KEY");
        String secret = System.getenv("REDDIT_API_SECRET");

        String authorizationStr = clientId + ":" + secret;
        String username = ((OptionalOutputLoginService)service).getUsername();
        String password = ((OptionalOutputLoginService)service).getPassword();
        String content = "grant_type=password" + "&username=" + username + "&password=" + password;
        String uri = "https://www.reddit.com/api/v1/access_token?" + content;
        String authorization = "Basic " + Base64.getUrlEncoder().encodeToString((authorizationStr).getBytes());
        JsonObject accessTokenResponse = HttpHelper.postRequestWithAuthorization(uri,authorization, "");

        return accessTokenResponse;
    }
}
