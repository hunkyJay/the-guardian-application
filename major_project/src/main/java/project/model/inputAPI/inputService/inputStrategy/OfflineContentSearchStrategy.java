package project.model.inputAPI.inputService.inputStrategy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.inputAPI.inputService.InputAPIService;

/**
 *  Offline strategy injected into Content Search service
 */
public class OfflineContentSearchStrategy implements InputServiceStrategy {
    @Override
    public JsonObject behave(InputAPIService service) {
        String response = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":4,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":1,\"orderBy\":\"relevance\",\"results\":[{\"id\":\"technology/2020/sep/11/apple-one-subscription-bundle\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2020-09-11T15:44:17Z\",\"webTitle\":\"Apple One services bundle could be launched within days\",\"webUrl\":\"https://www.theguardian.com/technology/2020/sep/11/apple-one-subscription-bundle\",\"apiUrl\":\"https://content.guardianapis.com/technology/2020/sep/11/apple-one-subscription-bundle\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2019/sep/10/apple-arcade-launch-netflix-for-games-will-cost-499-a-month\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2019-09-10T17:48:41Z\",\"webTitle\":\"Apple Arcade: game streaming service will cost £4.99 a month\",\"webUrl\":\"https://www.theguardian.com/technology/2019/sep/10/apple-arcade-launch-netflix-for-games-will-cost-499-a-month\",\"apiUrl\":\"https://content.guardianapis.com/technology/2019/sep/10/apple-arcade-launch-netflix-for-games-will-cost-499-a-month\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"games/2020/may/06/beyond-blue-review-blue-planet-ii-the-game\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2020-05-06T12:00:54Z\",\"webTitle\":\"Beyond Blue review: Blue Planet II, the game\",\"webUrl\":\"https://www.theguardian.com/games/2020/may/06/beyond-blue-review-blue-planet-ii-the-game\",\"apiUrl\":\"https://content.guardianapis.com/games/2020/may/06/beyond-blue-review-blue-planet-ii-the-game\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"games/2019/mar/27/apple-arcade-v-google-stadia-which-is-the-future-for-video-games\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2019-03-27T14:15:15Z\",\"webTitle\":\"Apple Arcade v Google Stadia: which is the future for video games?\",\"webUrl\":\"https://www.theguardian.com/games/2019/mar/27/apple-arcade-v-google-stadia-which-is-the-future-for-video-games\",\"apiUrl\":\"https://content.guardianapis.com/games/2019/mar/27/apple-arcade-v-google-stadia-which-is-the-future-for-video-games\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"}]}}";
        JsonObject contentsResponse = (JsonObject) JsonParser.parseString(response);

        return contentsResponse;
    }
}
