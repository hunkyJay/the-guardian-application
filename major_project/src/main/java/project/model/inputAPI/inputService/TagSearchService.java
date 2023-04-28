package project.model.inputAPI.inputService;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import project.model.entity.Entity;
import project.model.entity.Tag;
import project.model.entity.TagSearch;
import project.model.inputAPI.inputService.inputStrategy.InputServiceStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * The service class used to search tags by keywords
 */
public class TagSearchService implements InputAPIService {
    private String token;
    private String tagKeyword;
    private InputServiceStrategy tagSearchStrategy;

    public TagSearchService(InputServiceStrategy tagSearchStrategy, String token, String tagKeyword) {
        this.token = token;
        this.tagKeyword = tagKeyword;
        this.tagSearchStrategy = tagSearchStrategy;
    }

    /**
     * Search tags
     * @return Entity return tagSearch entity consist of tag keyword and matching tags
     */
    @Override
    public Entity operate() {
        JsonObject tagSearchResponse = tagSearchStrategy.behave(this);
        TagSearch tagSearch = null;

        if(tagSearchResponse != null && tagSearchResponse.get("response") != null) {
            JsonObject resultObject = tagSearchResponse.get("response").getAsJsonObject();
            if(resultObject != null) {
                //User gson to convert json object to TagSearch object
                Gson gson = new Gson();
                JsonArray tagSearchObjects = resultObject.get("results").getAsJsonArray();
//                System.out.println(tagSearchObjects);//

                List<Tag> relatedTags = new ArrayList<>();
                for (JsonElement obj : tagSearchObjects) {
                    Tag t = gson.fromJson(obj, Tag.class);
                    relatedTags.add(t);
                }
                tagSearch = new TagSearch(tagKeyword, relatedTags);
            }
        }
        return tagSearch;
    }

    /**
     * Get the input api token for this tag search service
     * @return The token String
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * Get the key word used to search tags
     * @return The keyword String
     */
    @Override
    public String getKeyword() {
        return tagKeyword;
    }
}
