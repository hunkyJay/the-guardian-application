package project.model.inputAPI.inputService;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import project.model.entity.Content;
import project.model.entity.Entity;
import project.model.entity.Tag;
import project.model.inputAPI.inputService.inputStrategy.InputServiceStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * The service class used for getting related contents of a tag
 */
public class TagContentsService implements InputAPIService {
    private String token;
    private String tagID;
    private InputServiceStrategy tagContentsStrategy;

    public TagContentsService(InputServiceStrategy tagContentsStrategy, String token, String tag) {
        this.token = token;
        this.tagID = tag;
        this.tagContentsStrategy = tagContentsStrategy;
    }

    /**
     * Search tag contents
     * @return Entity return tag with contents searched
     */
    @Override
    public Entity operate() {
        JsonObject tagContentsResponse = tagContentsStrategy.behave(this);
        Tag tag = null;

        if(tagContentsResponse != null && tagContentsResponse.get("response") != null) {
            JsonObject resultObject = tagContentsResponse.get("response").getAsJsonObject();
            if(resultObject != null) {
                //Get the tag object by tagID
                JsonObject tagObject = resultObject.get("tag").getAsJsonObject();
                String tagWebUrl = tagObject.get("webUrl").getAsString();

                //User gson to convert json object to User object
                Gson gson = new Gson();
                JsonArray contentObjects = resultObject.get("results").getAsJsonArray();
//                System.out.println(contentObjects);//

                List<Content> relatedContents = new ArrayList<>();
                for (JsonElement obj : contentObjects) {
                    Content c = gson.fromJson(obj, Content.class);
                    relatedContents.add(c);
                }
                //if (relatedContents.size() == 0) return null;
                //If there is no contents of the tag, just show tag with empty content list
                tag = new Tag(tagID, tagWebUrl);
                tag.setMatchingContents(relatedContents);
            }
        }
        return tag;
    }

    /**
     * Get the input api token for this tag contents search service
     * @return The token String
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * Get the tag id for all current contents
     * @return The tag id String
     */
    @Override
    public String getKeyword() {
        return tagID;
    }
}
