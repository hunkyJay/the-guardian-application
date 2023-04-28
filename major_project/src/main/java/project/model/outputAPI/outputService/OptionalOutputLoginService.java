package project.model.outputAPI.outputService;

import com.google.gson.JsonObject;
import project.model.entity.Entity;
import project.model.entity.User;
import project.model.outputAPI.outputService.outputStrategy.OutputServiceStrategy;

/**
 * The service class used to loin the optional output api
 */
public class OptionalOutputLoginService implements OutputAPIService {
    private String username;
    private String password;
    private String token;
    private OutputServiceStrategy optionalOutputLoginStrategy;


    public OptionalOutputLoginService(String username, String password, OutputServiceStrategy optionalOutputLoginStrategy ) {
        this.username = username;
        this.password = password;
        this.optionalOutputLoginStrategy = optionalOutputLoginStrategy;
    }

    /**
     * Login the optional output api
     * @return Entity return the User entity if successfully login
     */
    @Override
    public Entity operate() {
        User user = null;
        JsonObject accessTokenResponse = optionalOutputLoginStrategy.behave(this);
        if(accessTokenResponse != null && accessTokenResponse.get("access_token")!= null) {
            String accessToken = accessTokenResponse.get("access_token").getAsString();
            user = new User(username,"developer");
            token = accessToken;
            user.setToken(accessToken);
        }

        return user;
    }


    /**
     * Get the username for this optional output service
     * @return The username String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the password for this optional output service
     * @return The password String
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getOutputContent() {
        return null;
    }
}
