package project.model.inputAPI.inputService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import project.model.entity.Entity;
import project.model.entity.RegisterInfo;
import project.model.inputAPI.inputService.inputStrategy.InputServiceStrategy;

/**
 * The service class used for registering token
 */
public class RegisterGuardianService implements InputAPIService {
    private InputServiceStrategy registerStrategy;

    public RegisterGuardianService(InputServiceStrategy registerStrategy) {
        this.registerStrategy = registerStrategy;
    }

    /**
     * Register operation for the Guardian
     * @return Entity return register information entity
     */
    @Override
    public Entity operate() {
        JsonObject registerResponse = registerStrategy.behave(this);
        RegisterInfo registerInfo = null;

        if(registerResponse != null) {
            //User gson to convert json object to RegisterInfo object
            Gson gson = new Gson();
            JsonObject registerObject = registerResponse.get("response").getAsJsonObject();
//            System.out.println(registerObject);//
            registerInfo = gson.fromJson(registerObject, RegisterInfo.class);
        }

        return registerInfo;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public String getKeyword() {
        return null;
    }

}
