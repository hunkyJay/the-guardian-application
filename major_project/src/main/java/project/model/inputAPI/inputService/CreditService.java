package project.model.inputAPI.inputService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import project.model.entity.Entity;
import project.model.entity.SearchCreditRule;
import project.model.inputAPI.inputService.inputStrategy.InputServiceStrategy;
/**
 * The service class used to get the current search credit rule
 */
public class CreditService implements InputAPIService{
    private InputServiceStrategy creditStrategy;
    private String token;

    public CreditService(InputServiceStrategy creditStrategy, String token) {
        this.creditStrategy = creditStrategy;
        this.token = token;
    }

    /**
     * Get the current search credit rule
     * @return Entity return the SearchCreditRule entity as result
     */
    @Override
    public Entity operate() {
        JsonObject creditRuleObj = creditStrategy.behave(this);
        SearchCreditRule searchCreditRule = new Gson().fromJson(creditRuleObj, SearchCreditRule.class);
        return searchCreditRule;
    }

    /**
     * Get the input api token logged in by user
     * @return The token String
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * The keyword used for functions of the service
     * @return String null
     */
    @Override
    public String getKeyword() {
        return null;
    }
}
