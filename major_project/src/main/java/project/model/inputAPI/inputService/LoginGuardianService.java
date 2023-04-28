package project.model.inputAPI.inputService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import project.model.entity.Entity;
import project.model.entity.User;
import project.model.inputAPI.inputService.inputStrategy.InputServiceStrategy;

/**
 * The service class used to log in with token
 */
public class LoginGuardianService implements InputAPIService {
    private InputServiceStrategy loginStrategy;
    private String token;

    public LoginGuardianService(InputServiceStrategy loginStrategy, String token) {
        this.loginStrategy = loginStrategy;
        this.token = token;
    }


    /**
     * Get the input api token to start using the api
     * @return The token String
     */
    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getKeyword() {
        return null;
    }

    /**
     * Login the Guardian
     * @return Entity return user entity if successfully login
     */
    @Override
    public Entity operate() {
        JsonObject loginResponse = loginStrategy.behave(this);
        User user = null;

        if(loginResponse != null) {
            //User gson to convert json object to User object
            Gson gson = new Gson();
            JsonObject loginObject = loginResponse.get("response").getAsJsonObject();
//            System.out.println(loginObject);
            user = gson.fromJson(loginObject, User.class);
            user.setToken(token);
        }

        return user;
    }
}
