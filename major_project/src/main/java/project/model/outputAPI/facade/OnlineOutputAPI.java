package project.model.outputAPI.facade;

import project.model.entity.Entity;
import project.model.entity.User;
import project.model.outputAPI.outputService.OptionalOutputLoginService;
import project.model.outputAPI.outputService.OptionalOutputService;
import project.model.outputAPI.outputService.OutputService;
import project.model.outputAPI.outputService.outputStrategy.OnlineOutputStrategy;
import project.model.outputAPI.outputService.outputStrategy.OptionalOutputLoginStrategy;
import project.model.outputAPI.outputService.outputStrategy.OptionalOutputStrategy;

/**
 * The online mode output api facade
 */
public class OnlineOutputAPI implements OutputAPI {
    private User currentUser;

    @Override
    public Entity outputReport(String clientID, String outputQRCode) {
        OutputService outputService = new OutputService(new OnlineOutputStrategy(), clientID, outputQRCode);
        return outputService.operate();
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public Entity login(String username, String password) {
        OptionalOutputLoginService optionalOutputLoginService = new OptionalOutputLoginService(username, password, new OptionalOutputLoginStrategy());
        currentUser = (User) optionalOutputLoginService.operate();
        return currentUser;
    }

    @Override
    public Entity optionalOutputReport(String username, String accessToken, String output) {
        OptionalOutputService optionalOutputService = new OptionalOutputService(username, accessToken, output, new OptionalOutputStrategy());
        return optionalOutputService.operate();
    }

    @Override
    public String getAccessToken() {
        if(currentUser == null) return null;
        return currentUser.getToken();
    }

    @Override
    public String getUsername() {
        if(currentUser == null) return null;
        return currentUser.getStatus();
    }

}
