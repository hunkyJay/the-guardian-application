package project.model.outputAPI.facade;

import project.model.entity.Entity;
import project.model.outputAPI.outputService.OutputService;
import project.model.outputAPI.outputService.outputStrategy.OfflineOutputStrategy;

/**
 * The offline mode output api facade
 */
public class OfflineOutputAPI implements OutputAPI {
    @Override
    public Entity outputReport(String clientID, String outputQRCode) {
        OutputService outputService = new OutputService(new OfflineOutputStrategy(), clientID, outputQRCode);
        return outputService.operate();
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public Entity login(String username, String password) {
        return null;
    }

    @Override
    public Entity optionalOutputReport(String username, String accessToken, String output) {
        return null;
    }

    @Override
    public String getAccessToken() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }
}
