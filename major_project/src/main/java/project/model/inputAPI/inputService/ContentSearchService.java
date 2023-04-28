package project.model.inputAPI.inputService;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import project.model.entity.*;
import project.model.inputAPI.inputService.inputStrategy.InputServiceStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * The service class used to search content by keywords
 */
public class ContentSearchService implements InputAPIService {
    private String token;
    private String keyword;
    private InputServiceStrategy contentSearchStrategy;

    public ContentSearchService(InputServiceStrategy contentSearchStrategy, String token, String keyword) {
        this.contentSearchStrategy = contentSearchStrategy;
        this.token = token;
        this.keyword = keyword;
    }

    /**
     * Search content from the searching engine
     * @return Entity return the contentSearch entity as result
     */
    @Override
    public Entity operate() {
        JsonObject contentSearchResponse = contentSearchStrategy.behave(this);
        ContentSearch contentSearch = null;

        if(contentSearchResponse != null && contentSearchResponse.get("response") != null) {
            JsonObject resultObject = contentSearchResponse.get("response").getAsJsonObject();
            if(resultObject != null) {
                //User gson to convert json object to ContentSearch object
                Gson gson = new Gson();
                JsonArray contentSearchObjects = resultObject.get("results").getAsJsonArray();
//                System.out.println(contentSearchObjects);//

                List<Content> relatedContents = new ArrayList<>();
                for (JsonElement obj : contentSearchObjects) {
                    Content c = gson.fromJson(obj, Content.class);
                    relatedContents.add(c);
                }
                contentSearch = new ContentSearch(keyword, relatedContents);
            }
        }
        return contentSearch;
    }

    /**
     * Get the input api token for this content search service
     * @return The token String
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * The searching keyword used for this content search service
     * @return The keyword String
     */
    @Override
    public String getKeyword() {
        return keyword;
    }
}
