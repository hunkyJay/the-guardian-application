package project.model.outputAPI.outputService;

import project.model.entity.Entity;

/**
 * The output api service interface
 */
public interface OutputAPIService {
    /**
     * The api service to perform specific service function
     * @return Entity return the Entity object after converting Json results
     */
    Entity operate();

    /**
     * Get the api token or key
     * @return the token String to start using api
     */
    String getToken();


    /**
     * Get the content to output
     * @return String return the output content string
     */
    String getOutputContent();

}
