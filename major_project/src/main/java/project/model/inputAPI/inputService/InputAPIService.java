package project.model.inputAPI.inputService;

import project.model.entity.Entity;

/**
 * The input api service interface
 */
public interface InputAPIService {
    /**
     * The api service to perform its service function
     * @return Entity return the Entity object after converting Json results
     */
    Entity operate();

    /**
     * Get the api token or key
     * @return the token String to start using api
     */
    String getToken();

    /**
     * The keyword used for functions of the service
     * @return String the keyword used
     */
    String getKeyword();

}
