package project.model.inputAPI.inputService;

import project.model.entity.Entity;
import project.model.inputAPI.inputService.inputStrategy.InputServiceStrategy;

/**
 * The service class used to clear cache data
 */
public class ClearCacheService implements InputAPIService {
    private InputServiceStrategy clearCacheStrategy;

    public ClearCacheService(InputServiceStrategy clearCacheStrategy) {
        this.clearCacheStrategy = clearCacheStrategy;
    }

    /**
     * Clear searching cache
     * @return Entity return the cache clearing information
     */
    @Override
    public Entity operate() {
        clearCacheStrategy.behave(this);
        return null;
    }

    @Override
    public String getToken() {
        //Currently, storing user token is not required
        return null;
    }

    @Override
    public String getKeyword() {
        return null;
    }
}
