package project.model.outputAPI.facade;

import project.model.entity.Entity;

/**
 * Facade interface for the output api
 */
public interface OutputAPI {

    /**
     * Output the short form report to the output api
     * @param clientID The user's token (client id) for output api
     * @param outputQRCode The output QR code encoded in 64base format String
     * @return An Entity object (Output) with output file information
     */
    Entity outputReport(String clientID, String outputQRCode);

    /**
     * Check whether the api is online mode
     *
     * @return true online, false offline
     */
    boolean isOnline();

    /**
     * Login for the optional output function
     * @param username The username to login
     * @param password The password to login
     * @return The access token String if login successfully
     */
    Entity login(String username, String password);

    /**
     * Another optional output feature
     * @param username The logged-in username
     * @param accessToken The accessToken
     * @return An Entity object (OptionalOutput) with output result information
     */
    Entity optionalOutputReport(String username, String accessToken, String output);

    /**
     *
     * @return get the access token String for the optional output function
     */
    String getAccessToken();

    /**
     *
     * @return get the currently logged-in username
     */
    String getUsername();

}
