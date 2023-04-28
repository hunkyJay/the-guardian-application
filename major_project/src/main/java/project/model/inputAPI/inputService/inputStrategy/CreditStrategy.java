package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.entity.SearchCreditRule;
import project.model.inputAPI.inputService.InputAPIService;
/**
 *  The concrete strategy injected into Credit Service
 */
public class CreditStrategy implements InputServiceStrategy{
    @Override
    public JsonObject behave(InputAPIService service) {
        SearchCreditRule searchCreditRule = new SearchCreditRule(1, 10, 1);
        String tagJson = new Gson().toJson(searchCreditRule);
        JsonObject result = (JsonObject) JsonParser.parseString(tagJson);

        return result;
    }
}
