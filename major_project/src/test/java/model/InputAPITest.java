package model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;
import project.model.entity.*;
import project.model.inputAPI.facade.InputAPI;
import project.model.inputAPI.facade.OfflineGuardian;
import project.model.inputAPI.facade.OnlineGuardian;
import project.model.inputAPI.inputService.*;
import project.model.inputAPI.inputService.inputStrategy.*;
import project.model.util.database.DBConnection;
import project.model.util.HttpHelper;
import project.model.util.database.dao.ContentDao;
import project.model.util.database.dao.ContentDaoFactory;
import project.model.util.database.dao.ContentDaoImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The junit tests for input api model
 */
public class InputAPITest {
    private InputAPI onlineInputAPI;
    private InputAPI offlineInputAPI;

    @BeforeEach
    void setUp() {
        onlineInputAPI = new OnlineGuardian();
        offlineInputAPI = new OfflineGuardian();
    }

    /**
     * Test successfully login the guard api in online mode
     */
    @Test
    public void onlineLoginSuccessfully(){
        String url = "https://content.guardianapis.com/?api-key=" + "valid token";
        JsonObject successfulLoginResponse = (JsonObject) JsonParser.parseString("{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":1}}");
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(successfulLoginResponse);

            Entity userEntity = onlineInputAPI.login("valid token");
//            assertTrue(userEntity instanceof User);
            User user = (User) userEntity;
            assertEquals("valid token", user.getToken());
            assertEquals("ok", user.getStatus());
            assertEquals("developer", user.getUserTier());
            assertEquals("Current User: " + "valid token" + "\n" +
                    "User Tier: " + "developer" + "\n" +
                    "Status: " + "ok" + "\n", user.getEntityInfo());
        }
    }

    /**
     * Test failed login the guard api in online mode
     */
    @Test
    public void onlineLoginFailed(){
        String url = "https://content.guardianapis.com/?api-key=" + "invalid token";
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(null);

            Entity userEntity = onlineInputAPI.login("invalid token");
            assertNull(userEntity);

            InputAPIService loginService = new LoginGuardianService(new OnlineLoginGuardianStrategy(),"invalid token");
            assertNull(loginService.getKeyword());
        }
    }

    /**
     * Test successfully login the guard api in offline mode
     */
    @Test
    public void offlineLoginSuccessfully(){
        String existedKey = System.getenv("INPUT_API_KEY");
        String testKey = "test";

        //Key from the environment variable
        Entity userEntity1 = offlineInputAPI.login(existedKey);
        User user1 = (User) userEntity1;
        assertEquals(existedKey, user1.getToken());
        assertEquals("ok", user1.getStatus());
        assertEquals("developer", user1.getUserTier());
        assertEquals("Current User: " + existedKey + "\n" +
                "User Tier: " + "developer" + "\n" +
                "Status: " + "ok" + "\n", user1.getEntityInfo());

        //Existing test key
        Entity userEntity2 = offlineInputAPI.login(testKey);
        User user2 = (User) userEntity2;
        assertEquals(testKey, user2.getToken());
        assertEquals("ok", user2.getStatus());
        assertEquals("developer", user2.getUserTier());
        assertEquals("Current User: " + testKey + "\n" +
                "User Tier: " + "developer" + "\n" +
                "Status: " + "ok" + "\n", user2.getEntityInfo());
        User userTest = new User(user2.getStatus(), user2.getUserTier());
    }

    /**
     * Test failed login the guard api in offline mode
     */
    @Test
    public void offlineLoginFailed() {
        Entity userEntity = offlineInputAPI.login("wrong key");
        assertNull(userEntity);

        Entity userEntity2 = offlineInputAPI.login("");
        assertNull(userEntity2);

        InputAPIService loginService = new LoginGuardianService(new OfflineLoginGuardianStrategy(),"wrong key");
        assertNull(loginService.getKeyword());
    }

    /**
     * Test register function of the input api in online mode
     */
    @Test
    public void onlineRegister(){
        Entity registerInfoEntity = onlineInputAPI.register();
        RegisterInfo registerInfo = (RegisterInfo) registerInfoEntity;
        assertEquals("Please go to the register website to create new key", registerInfo.getEntityInfo());
        assertEquals("https://open-platform.theguardian.com/access/", registerInfo.getLink());

        InputAPIService registerService = new RegisterGuardianService(new OnlineRegisterGuardianStrategy());
        assertNull(registerService.getKeyword());
        assertNull(registerService.getToken());
    }

    /**
     * Test register function of the input api in offline mode
     */
    @Test
    public void offlineRegister(){
        Entity registerInfoEntity = offlineInputAPI.register();
        RegisterInfo registerInfo = (RegisterInfo) registerInfoEntity;
        assertEquals("Successfully created the test key: test \nOr go to the website to create online.", registerInfo.getEntityInfo());
        assertEquals("https://open-platform.theguardian.com/access/", registerInfo.getLink());
        RegisterInfo registerInfo1 = new RegisterInfo(registerInfo.getEntityInfo(),registerInfo.getLink());

        InputAPIService registerService = new RegisterGuardianService(new OfflineRegisterGuardianStrategy());
        assertNull(registerService.getKeyword());
        assertNull(registerService.getToken());
    }

    /**
     * Test search related tags function of the input api in online mode
     */
    @Test
    public void onlineSearchRelatedTags() {
        onlineInputAPI.setCurrentToken("test");
        //Search successfully
        String url = "https://content.guardianapis.com/tags?web-title=" + "apple" +"&api-key=" + onlineInputAPI.getCurrentToken();
        JsonObject tagSearchObj = (JsonObject) JsonParser.parseString(
                "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":23,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":3,\"results\":[{\"id\":\"games/apple-arcade\",\"type\":\"keyword\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webTitle\":\"Apple Arcade\",\"webUrl\":\"https://www.theguardian.com/games/apple-arcade\",\"apiUrl\":\"https://content.guardianapis.com/games/apple-arcade\"},{\"id\":\"global/series/bite-of-the-apple\",\"type\":\"series\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webTitle\":\"Bite of the apple\",\"webUrl\":\"https://www.theguardian.com/global/series/bite-of-the-apple\",\"apiUrl\":\"https://content.guardianapis.com/global/series/bite-of-the-apple\",\"description\":\"Guardian journalist Paul Owen's monthly postcard from New York City.\"},{\"id\":\"lifeandstyle/series/an-apple-a-day\",\"type\":\"series\",\"sectionId\":\"lifeandstyle\",\"sectionName\":\"Life and style\",\"webTitle\":\"An apple a day\",\"webUrl\":\"https://www.theguardian.com/lifeandstyle/series/an-apple-a-day\",\"apiUrl\":\"https://content.guardianapis.com/lifeandstyle/series/an-apple-a-day\",\"description\":\"<p>A series exploring everyday health and wellbeing, with stories about food, products and lifestyle trends</p>\"},{\"id\":\"media/apple-tv-plus\",\"type\":\"keyword\",\"sectionId\":\"media\",\"sectionName\":\"Media\",\"webTitle\":\"Apple TV+\",\"webUrl\":\"https://www.theguardian.com/media/apple-tv-plus\",\"apiUrl\":\"https://content.guardianapis.com/media/apple-tv-plus\"},{\"id\":\"music/fionaapple\",\"type\":\"keyword\",\"sectionId\":\"music\",\"sectionName\":\"Music\",\"webTitle\":\"Fiona Apple\",\"webUrl\":\"https://www.theguardian.com/music/fionaapple\",\"apiUrl\":\"https://content.guardianapis.com/music/fionaapple\"},{\"id\":\"profile/ashton-applewhite\",\"type\":\"contributor\",\"webTitle\":\"Ashton Applewhite\",\"webUrl\":\"https://www.theguardian.com/profile/ashton-applewhite\",\"apiUrl\":\"https://content.guardianapis.com/profile/ashton-applewhite\",\"bio\":\"<p>Ashton Applewhite is the author of <em>This Chair Rocks: A Manifesto Against Ageism</em>, coming out from Celadon Books in March 2019, and a leading spokeswoman for a movement to mobilize against discrimination on the basis of age</p><p><br></p>\",\"firstName\":\"Ashton\",\"lastName\":\"Applewhite\"},{\"id\":\"profile/chia-jung-apple-c-fardel\",\"type\":\"contributor\",\"webTitle\":\"Chia-Jung (Apple) C.Fardel\",\"webUrl\":\"https://www.theguardian.com/profile/chia-jung-apple-c-fardel\",\"apiUrl\":\"https://content.guardianapis.com/profile/chia-jung-apple-c-fardel\",\"bio\":\"<p>Chia-Jung (Apple) C.Fardel is an interactive developer for the Guardian. She likes User Interface Engineering and Data Visualisation</p>\",\"firstName\":\"chia-jung\",\"lastName\":\"(apple)c.fardel\"},{\"id\":\"profile/emily-apple\",\"type\":\"contributor\",\"webTitle\":\"Emily Apple\",\"webUrl\":\"https://www.theguardian.com/profile/emily-apple\",\"apiUrl\":\"https://content.guardianapis.com/profile/emily-apple\",\"bio\":\"<p>Emily Apple is the co-founder of <a href=\\\"http://www.fitwatch.org.uk/\\\">Fitwatch</a>, and a writer, activist and mother, living in Cornwall</p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/pictures/2009/6/22/1245670519087/emily.jpg\",\"firstName\":\"apple\",\"lastName\":\"emily\"},{\"id\":\"profile/gabrielle-appleby\",\"type\":\"contributor\",\"webTitle\":\"Gabrielle Appleby\",\"webUrl\":\"https://www.theguardian.com/profile/gabrielle-appleby\",\"apiUrl\":\"https://content.guardianapis.com/profile/gabrielle-appleby\",\"bio\":\"<p>Gabrielle Appleby is a constitutional lawyer working in the Gilbert + Tobin Centre, University of New South Wales. She provided pro bono legal assistance at the Referendum Council’s Regional Dialogues and the Uluru Convention</p>\"},{\"id\":\"profile/johnappleby\",\"type\":\"contributor\",\"webTitle\":\"John Appleby\",\"webUrl\":\"https://www.theguardian.com/profile/johnappleby\",\"apiUrl\":\"https://content.guardianapis.com/profile/johnappleby\",\"bio\":\"<p>John Appleby is director of research and chief economist at the Nuffield Trust</p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/pictures/2008/12/23/1230047295778/john_appleby_140x140.jpg\",\"firstName\":\"John\",\"lastName\":\"Appleby\"}]}}");
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(tagSearchObj);

            Entity tagSearchEntity = onlineInputAPI.searchRelatedTags("test","apple");
            TagSearch tagSearch = (TagSearch) tagSearchEntity;
            assertEquals("apple", tagSearch.getKeyword());
            assertEquals(10, tagSearch.getRelatedTags().size());
            assertEquals("Your tags searching keywords: apple", tagSearch.getEntityInfo());
        }

        //Search non result
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(null);
            Entity tagSearchEntity = onlineInputAPI.searchRelatedTags("test","apple");
            assertNull(tagSearchEntity);
        }

        //Search error
        JsonObject errorObj = (JsonObject) JsonParser.parseString("{\"error\": error}");
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(errorObj);
            Entity tagSearchEntity = onlineInputAPI.searchRelatedTags("test","apple");
            assertNull(tagSearchEntity);
        }

    }

    /**
     * Test search related tags function of the input api in offline mode
     */
    @Test
    public void offlineSearchRelatedTags() {
        offlineInputAPI.setCurrentToken("test");
        Entity tagSearchEntity = offlineInputAPI.searchRelatedTags(offlineInputAPI.getCurrentToken(),"apple");
//        assertTrue(tagSearchEntity instanceof TagSearch);
        TagSearch tagSearch = (TagSearch) tagSearchEntity;
        assertEquals("apple", tagSearch.getKeyword());
        assertEquals(10, tagSearch.getRelatedTags().size());
        assertEquals("Your tags searching keywords: apple", tagSearch.getEntityInfo());
    }

    /**
     * Test get tag contents of the input api in online mode
     */
    @Test
    public void onlineGetTagContents(){
        onlineInputAPI.setCurrentToken("test");
        String url = "https://content.guardianapis.com/" + "games/apple-arcade" + "?page-size=100&api-key=" + onlineInputAPI.getCurrentToken();
        String response = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":9,\"startIndex\":1,\"pageSize\":5,\"currentPage\":1,\"pages\":2,\"orderBy\":\"newest\",\"tag\":{\"id\":\"games/apple-arcade\",\"type\":\"keyword\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webTitle\":\"Apple Arcade\",\"webUrl\":\"https://www.theguardian.com/games/apple-arcade\",\"apiUrl\":\"https://content.guardianapis.com/games/apple-arcade\"},\"results\":[{\"id\":\"games/2022/feb/17/the-15-best-games-on-apple-arcade\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2022-02-17T10:00:33Z\",\"webTitle\":\"The 15 best games on Apple Arcade\",\"webUrl\":\"https://www.theguardian.com/games/2022/feb/17/the-15-best-games-on-apple-arcade\",\"apiUrl\":\"https://content.guardianapis.com/games/2022/feb/17/the-15-best-games-on-apple-arcade\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"games/2021/apr/22/clap-hanz-golf-review-virtual-golf-thats-fun-for-everybody\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2021-04-22T07:00:33Z\",\"webTitle\":\"Clap Hanz Golf review virtual golf that’s fun for everybody\",\"webUrl\":\"https://www.theguardian.com/games/2021/apr/22/clap-hanz-golf-review-virtual-golf-thats-fun-for-everybody\",\"apiUrl\":\"https://content.guardianapis.com/games/2021/apr/22/clap-hanz-golf-review-virtual-golf-thats-fun-for-everybody\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"games/2021/apr/14/fantasian-review-iphone-game-apple-arcade\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2021-04-14T11:00:37Z\",\"webTitle\":\"Fantasian review Beautiful, if not always bold iPhone game\",\"webUrl\":\"https://www.theguardian.com/games/2021/apr/14/fantasian-review-iphone-game-apple-arcade\",\"apiUrl\":\"https://content.guardianapis.com/games/2021/apr/14/fantasian-review-iphone-game-apple-arcade\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"games/2021/feb/25/nuts-a-surveillance-mystery-review-ios-pc-nintendo-switch\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2021-02-25T10:00:24Z\",\"webTitle\":\"Nuts: a Surveillance Mystery review squirrel snapper's delight takes a dark turn\",\"webUrl\":\"https://www.theguardian.com/games/2021/feb/25/nuts-a-surveillance-mystery-review-ios-pc-nintendo-switch\",\"apiUrl\":\"https://content.guardianapis.com/games/2021/feb/25/nuts-a-surveillance-mystery-review-ios-pc-nintendo-switch\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"technology/2020/sep/11/apple-one-subscription-bundle\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2020-09-11T15:44:17Z\",\"webTitle\":\"Apple One services bundle could be launched within days\",\"webUrl\":\"https://www.theguardian.com/technology/2020/sep/11/apple-one-subscription-bundle\",\"apiUrl\":\"https://content.guardianapis.com/technology/2020/sep/11/apple-one-subscription-bundle\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"}],\"leadContent\":[]}}";
        JsonObject contentsResponse = (JsonObject) JsonParser.parseString(response);

        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(contentsResponse);

            Entity tagEntity = onlineInputAPI.getTagContents("test","games/apple-arcade");
            Tag tag = (Tag) tagEntity;
            assertEquals("games/apple-arcade", tag.getId());
            assertEquals("https://www.theguardian.com/games/apple-arcade", tag.getWebUrl());
            assertEquals(5, tag.getMatchingContents().size());
            assertEquals("Tag: games/apple-arcade"
                    +"\nWeb Url: https://www.theguardian.com/games/apple-arcade", tag.getEntityInfo());
            assertEquals("Tag: games/apple-arcade"
                    +"\nWeb Url: https://www.theguardian.com/games/apple-arcade", tag.getEntityInfo());
        }

        //Search non result
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(null);

            Entity tagEntity = onlineInputAPI.getTagContents("test","games/apple-arcade");
            assertNull(tagEntity);
        }

        //Search error
        JsonObject errorObj = (JsonObject) JsonParser.parseString("{\"error\": error}");
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(errorObj);
            Entity tagEntity = onlineInputAPI.getTagContents("test","games/apple-arcade");
            assertNull(tagEntity);
        }
    }

    /**
     * Test get tag contents of the input api in offline mode
     */
    @Test
    public void offlineGetTagContents(){
        offlineInputAPI.setCurrentToken("test");
        Entity tagEntity = offlineInputAPI.getTagContents(onlineInputAPI.getCurrentToken(),"games/apple-arcade");
        Tag tag = (Tag) tagEntity;
        assertEquals("games/apple-arcade", tag.getId());
        assertEquals("https://www.theguardian.com/games/apple-arcade", tag.getWebUrl());
        assertEquals(5, tag.getMatchingContents().size());
        assertEquals("Tag: games/apple-arcade"
                +"\nWeb Url: https://www.theguardian.com/games/apple-arcade", tag.getEntityInfo());
        assertEquals("Tag: games/apple-arcade"
                +"\nWeb Url: https://www.theguardian.com/games/apple-arcade", tag.getEntityInfo());
    }

    /**
     * Test search contents function of the input api in online mode
     */
    @Test
    public void onlineSearchContents(){
        onlineInputAPI.setCurrentToken("test");
        String contentKeywords = "content keywords";
        String url = "https://content.guardianapis.com/search?" + contentKeywords + "&page-size=50&api-key=" + onlineInputAPI.getCurrentToken();
        //Search successfully
        String response = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":4,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":1,\"orderBy\":\"relevance\",\"results\":[{\"id\":\"technology/2020/sep/11/apple-one-subscription-bundle\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2020-09-11T15:44:17Z\",\"webTitle\":\"Apple One services bundle could be launched within days\",\"webUrl\":\"https://www.theguardian.com/technology/2020/sep/11/apple-one-subscription-bundle\",\"apiUrl\":\"https://content.guardianapis.com/technology/2020/sep/11/apple-one-subscription-bundle\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2019/sep/10/apple-arcade-launch-netflix-for-games-will-cost-499-a-month\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2019-09-10T17:48:41Z\",\"webTitle\":\"Apple Arcade: game streaming service will cost £4.99 a month\",\"webUrl\":\"https://www.theguardian.com/technology/2019/sep/10/apple-arcade-launch-netflix-for-games-will-cost-499-a-month\",\"apiUrl\":\"https://content.guardianapis.com/technology/2019/sep/10/apple-arcade-launch-netflix-for-games-will-cost-499-a-month\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"games/2020/may/06/beyond-blue-review-blue-planet-ii-the-game\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2020-05-06T12:00:54Z\",\"webTitle\":\"Beyond Blue review: Blue Planet II, the game\",\"webUrl\":\"https://www.theguardian.com/games/2020/may/06/beyond-blue-review-blue-planet-ii-the-game\",\"apiUrl\":\"https://content.guardianapis.com/games/2020/may/06/beyond-blue-review-blue-planet-ii-the-game\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"games/2019/mar/27/apple-arcade-v-google-stadia-which-is-the-future-for-video-games\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2019-03-27T14:15:15Z\",\"webTitle\":\"Apple Arcade v Google Stadia: which is the future for video games?\",\"webUrl\":\"https://www.theguardian.com/games/2019/mar/27/apple-arcade-v-google-stadia-which-is-the-future-for-video-games\",\"apiUrl\":\"https://content.guardianapis.com/games/2019/mar/27/apple-arcade-v-google-stadia-which-is-the-future-for-video-games\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"}]}}";
        JsonObject contentsResponse = (JsonObject) JsonParser.parseString(response);

        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(contentsResponse);

            Entity contentSearchEntity = onlineInputAPI.searchContents("test",contentKeywords);
            ContentSearch contentSearch = (ContentSearch) contentSearchEntity;
            assertEquals("content keywords", contentSearch.getKeyword());
            assertEquals(4, contentSearch.getRelatedContents().size());
            assertEquals("Your contents searching keywords: " + contentKeywords, contentSearch.getEntityInfo());
        }

        //Search non result
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(null);

            Entity contentSearchEntity = onlineInputAPI.searchContents("test",contentKeywords);
            assertNull(contentSearchEntity);
        }

        //Search error
        JsonObject errorObj = (JsonObject) JsonParser.parseString("{\"error\": error}");
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(errorObj);
            Entity contentSearchEntity = onlineInputAPI.searchContents("test",contentKeywords);
            assertNull(contentSearchEntity);
        }

    }

    /**
     * Test search contents function of the input api in offline mode
     */
    @Test
    public void offlineSearchContents(){
        offlineInputAPI.setCurrentToken("test");
        String contentKeywords = "content keywords";

        Entity contentSearchEntity = offlineInputAPI.searchContents(offlineInputAPI.getCurrentToken(),contentKeywords);
        ContentSearch contentSearch = (ContentSearch) contentSearchEntity;
        assertEquals("content keywords", contentSearch.getKeyword());
        assertEquals(4, contentSearch.getRelatedContents().size());
        assertEquals("Your contents searching keywords: " + contentKeywords, contentSearch.getEntityInfo());
    }

    /**
     * Test get short form and long form results of the input api in online mode
     */
    @Test
    public void onlineResults(){
        assertNull(onlineInputAPI.getLongFormResult());
        assertNull(onlineInputAPI.getShortFormResult());
        assertTrue(onlineInputAPI.isOnline());

        onlineInputAPI.setCurrentToken("test");
        String url = "https://content.guardianapis.com/" + "games/apple-arcade" + "?page-size=100&api-key=" + onlineInputAPI.getCurrentToken();
        String response = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":9,\"startIndex\":1,\"pageSize\":5,\"currentPage\":1,\"pages\":2,\"orderBy\":\"newest\",\"tag\":{\"id\":\"games/apple-arcade\",\"type\":\"keyword\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webTitle\":\"Apple Arcade\",\"webUrl\":\"https://www.theguardian.com/games/apple-arcade\",\"apiUrl\":\"https://content.guardianapis.com/games/apple-arcade\"},\"results\":[{\"id\":\"games/2022/feb/17/the-15-best-games-on-apple-arcade\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2022-02-17T10:00:33Z\",\"webTitle\":\"The 15 best games on Apple Arcade\",\"webUrl\":\"https://www.theguardian.com/games/2022/feb/17/the-15-best-games-on-apple-arcade\",\"apiUrl\":\"https://content.guardianapis.com/games/2022/feb/17/the-15-best-games-on-apple-arcade\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"games/2021/apr/22/clap-hanz-golf-review-virtual-golf-thats-fun-for-everybody\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2021-04-22T07:00:33Z\",\"webTitle\":\"Clap Hanz Golf review virtual golf that’s fun for everybody\",\"webUrl\":\"https://www.theguardian.com/games/2021/apr/22/clap-hanz-golf-review-virtual-golf-thats-fun-for-everybody\",\"apiUrl\":\"https://content.guardianapis.com/games/2021/apr/22/clap-hanz-golf-review-virtual-golf-thats-fun-for-everybody\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"games/2021/apr/14/fantasian-review-iphone-game-apple-arcade\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2021-04-14T11:00:37Z\",\"webTitle\":\"Fantasian review Beautiful, if not always bold iPhone game\",\"webUrl\":\"https://www.theguardian.com/games/2021/apr/14/fantasian-review-iphone-game-apple-arcade\",\"apiUrl\":\"https://content.guardianapis.com/games/2021/apr/14/fantasian-review-iphone-game-apple-arcade\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"games/2021/feb/25/nuts-a-surveillance-mystery-review-ios-pc-nintendo-switch\",\"type\":\"article\",\"sectionId\":\"games\",\"sectionName\":\"Games\",\"webPublicationDate\":\"2021-02-25T10:00:24Z\",\"webTitle\":\"Nuts: a Surveillance Mystery review squirrel snapper's delight takes a dark turn\",\"webUrl\":\"https://www.theguardian.com/games/2021/feb/25/nuts-a-surveillance-mystery-review-ios-pc-nintendo-switch\",\"apiUrl\":\"https://content.guardianapis.com/games/2021/feb/25/nuts-a-surveillance-mystery-review-ios-pc-nintendo-switch\",\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"technology/2020/sep/11/apple-one-subscription-bundle\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2020-09-11T15:44:17Z\",\"webTitle\":\"Apple One services bundle could be launched within days\",\"webUrl\":\"https://www.theguardian.com/technology/2020/sep/11/apple-one-subscription-bundle\",\"apiUrl\":\"https://content.guardianapis.com/technology/2020/sep/11/apple-one-subscription-bundle\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"}],\"leadContent\":[]}}";
        JsonObject contentsResponse = (JsonObject) JsonParser.parseString(response);

        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.getRequest(url)).thenReturn(contentsResponse);

            Entity tagEntity = onlineInputAPI.getTagContents("test", "games/apple-arcade");
            Tag selectedTag = (Tag) tagEntity;

            //Long form result
            onlineInputAPI.setLongFormResult(new LongFormResult(selectedTag, selectedTag.getMatchingContents()));
            LongFormResult longFormResult = (LongFormResult) onlineInputAPI.getLongFormResult();
            assertEquals("games/apple-arcade", longFormResult.getSelectedTag().getId());
            assertEquals("https://www.theguardian.com/games/apple-arcade", longFormResult.getSelectedTag().getWebUrl());
            assertEquals(5, longFormResult.getContentList().size());
            assertEquals("Tag: games/apple-arcade\n" +
                    "Web Url: https://www.theguardian.com/games/apple-arcadeThe 15 best games on Apple Arcade \n" +
                    "Clap Hanz Golf review virtual golf that’s fun for everybody \n" +
                    "Fantasian review Beautiful, if not always bold iPhone game \n" +
                    "Nuts: a Surveillance Mystery review squirrel snapper's delight takes a dark turn \n" +
                    "Apple One services bundle could be launched within days \n", longFormResult.getEntityInfo());

            //Short form result
            Content selectedContent = ((LongFormResult) onlineInputAPI.getLongFormResult()).getContentList().get(0);
            onlineInputAPI.setShortFormResult(new ShortFormResult(selectedTag, selectedContent));
            assertEquals("games/2022/feb/17/the-15-best-games-on-apple-arcade", selectedContent.getId());
            assertEquals("The 15 best games on Apple Arcade", selectedContent.getWebTitle());
            assertEquals("2022-02-17T10:00:33Z", selectedContent.getWebPublicationDate());
            assertEquals("https://www.theguardian.com/games/2022/feb/17/the-15-best-games-on-apple-arcade", selectedContent.getWebUrl());

            ShortFormResult shortFormResult = (ShortFormResult) onlineInputAPI.getShortFormResult();
            assertEquals("games/apple-arcade", shortFormResult.getSelectedTag().getId());
            assertEquals("games/2022/feb/17/the-15-best-games-on-apple-arcade", shortFormResult.getSelectedContent().getId());
            assertEquals("The 15 best games on Apple Arcade", shortFormResult.getSelectedContent().getWebTitle());
            assertEquals("2022-02-17T10:00:33Z", shortFormResult.getSelectedContent().getWebPublicationDate());
            assertEquals("https://www.theguardian.com/games/2022/feb/17/the-15-best-games-on-apple-arcade", shortFormResult.getSelectedContent().getWebUrl());
            assertEquals("Web Tile: The 15 best games on Apple Arcade\n" +
                    "Web Publication Date: 2022-02-17T10:00:33Z\n" +
                    "Web Url: https://www.theguardian.com/games/2022/feb/17/the-15-best-games-on-apple-arcade\n", shortFormResult.getSelectedContent().getEntityInfo());
        }
    }

    /**
     * Test get short form and long form results of the input api in offline mode
     */
    @Test
    public void offlineResults(){
        assertNull(offlineInputAPI.getLongFormResult());
        assertNull(offlineInputAPI.getShortFormResult());
        assertFalse(offlineInputAPI.isOnline());

        offlineInputAPI.setCurrentToken("test");
        Entity tagEntity = offlineInputAPI.getTagContents(onlineInputAPI.getCurrentToken(),"games/apple-arcade");
        Tag selectedTag = (Tag) tagEntity;

        //Long form result
        offlineInputAPI.setLongFormResult(new LongFormResult(selectedTag, selectedTag.getMatchingContents()));
        LongFormResult longFormResult = (LongFormResult) offlineInputAPI.getLongFormResult();
        assertEquals("games/apple-arcade", longFormResult.getSelectedTag().getId());
        assertEquals("https://www.theguardian.com/games/apple-arcade", longFormResult.getSelectedTag().getWebUrl());
        assertEquals(5, longFormResult.getContentList().size());
        assertEquals("Tag: games/apple-arcade\n" +
                "Web Url: https://www.theguardian.com/games/apple-arcadeThe 15 best games on Apple Arcade \n" +
                "Clap Hanz Golf review virtual golf that’s fun for everybody \n" +
                "Fantasian review Beautiful, if not always bold iPhone game \n" +
                "Nuts: a Surveillance Mystery review squirrel snapper's delight takes a dark turn \n" +
                "Apple One services bundle could be launched within days \n", longFormResult.getEntityInfo());

        //Short form result
        Content selectedContent = ((LongFormResult) offlineInputAPI.getLongFormResult()).getContentList().get(0);
        offlineInputAPI.setShortFormResult(new ShortFormResult(selectedTag, selectedContent));
        assertEquals("games/2022/feb/17/the-15-best-games-on-apple-arcade", selectedContent.getId());
        assertEquals("The 15 best games on Apple Arcade", selectedContent.getWebTitle());
        assertEquals("2022-02-17T10:00:33Z", selectedContent.getWebPublicationDate());
        assertEquals("https://www.theguardian.com/games/2022/feb/17/the-15-best-games-on-apple-arcade", selectedContent.getWebUrl());

        ShortFormResult shortFormResult = (ShortFormResult) offlineInputAPI.getShortFormResult();
        assertEquals("games/apple-arcade", shortFormResult.getSelectedTag().getId());
        assertEquals("games/2022/feb/17/the-15-best-games-on-apple-arcade", shortFormResult.getSelectedContent().getId());
        assertEquals("The 15 best games on Apple Arcade", shortFormResult.getSelectedContent().getWebTitle());
        assertEquals("2022-02-17T10:00:33Z", shortFormResult.getSelectedContent().getWebPublicationDate());
        assertEquals("https://www.theguardian.com/games/2022/feb/17/the-15-best-games-on-apple-arcade", shortFormResult.getSelectedContent().getWebUrl());
        assertEquals("Web Tile: The 15 best games on Apple Arcade\n" +
                "Web Publication Date: 2022-02-17T10:00:33Z\n" +
                "Web Url: https://www.theguardian.com/games/2022/feb/17/the-15-best-games-on-apple-arcade\n", shortFormResult.getSelectedContent().getEntityInfo());
    }

    /**
     * Test cache function of the input api
     */
    @Test
    public void tagContentsCache(){
        //Mock the database connection and ContentDao Factory
        DBConnection dbConnectionMock = mock(DBConnection.class);
        Connection connectionMock = mock(Connection.class);
        ContentDao contentDaoMock = mock(ContentDaoImpl.class);
        try (MockedStatic<DBConnection> mock = mockStatic(DBConnection.class);
             MockedStatic<ContentDaoFactory> factoryMock = mockStatic(ContentDaoFactory.class)) {
            //Mock the static method to get instance
            mock.when(() -> DBConnection.getInstance()).thenReturn(dbConnectionMock);
            factoryMock.when(() -> ContentDaoFactory.create()).thenReturn(contentDaoMock);
            when(dbConnectionMock.getConnection()).thenReturn(connectionMock);

            //Online mode
            onlineInputAPI.setCurrentToken("test");
            onlineInputAPI.createDatabase();
            when(contentDaoMock.queryContentsByTagId("testTagID")).thenReturn(new Tag("testTagID", null));
            assertNull(onlineInputAPI.getTagContentsCache("testTagID"));

            //Add a tag with contents to cache
            Tag testTag = new Tag("testTagID", "testTagUrl");
            List<Content> contents = new ArrayList<>();
            contents.add(new Content("testContentID", "testContentWeb", "publicationDate", "testContentUrl"));
            testTag.setMatchingContents(contents);
            onlineInputAPI.updateTagContentsCache(testTag);
            when(contentDaoMock.queryContentsByTagId("testTagID")).thenReturn(testTag);
            Tag tagQueried = (Tag) onlineInputAPI.getTagContentsCache("testTagID");
            assertEquals(1, tagQueried.getMatchingContents().size());
            //Update cache
            contents.add(new Content("testContentID2", "testContentWeb2", "publicationDate2", "testContentUrl2"));
            onlineInputAPI.updateTagContentsCache(testTag);
            when(contentDaoMock.queryContentsByTagId("testTagID")).thenReturn(testTag);
            Tag tagQueried2 = (Tag) onlineInputAPI.getTagContentsCache("testTagID");
            assertEquals(2, tagQueried2.getMatchingContents().size());
            //Clear cache
            assertNull(onlineInputAPI.clearCache());


            //Additional unit tests
            UpdateCacheService updateCacheService = new UpdateCacheService(new UpdateContentsCacheStrategy(), testTag);
            assertNull(updateCacheService.getToken());
            assertEquals("testTagID", updateCacheService.getKeyword());
            SearchCacheService searchCacheService = new SearchCacheService(new SearchContentsCacheStrategy(), "testTagID");
            assertEquals(null, searchCacheService.getToken());
            ClearCacheService clearCacheService = new ClearCacheService(new ClearTagContentsCacheStrategy());
            assertNull(clearCacheService.getKeyword());
            assertNull(clearCacheService.getToken());

            //Offline mode
            offlineInputAPI.createDatabase();
            assertNull(offlineInputAPI.updateTagContentsCache(testTag));
            assertNull(offlineInputAPI.getTagContentsCache("testTagID"));
            assertNull(offlineInputAPI.clearCache());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test current search credit rule
     */
    @Test
    public void currentCreditRule(){
        //Online mode
        SearchCreditRule creditRule = (SearchCreditRule) onlineInputAPI.currentCreditRule();
        assertEquals(1, creditRule.getMin());
        assertEquals(10, creditRule.getMax());
        assertEquals(1, creditRule.getConsumptionPerTime());
        assertEquals("The setting credit should be between 1 and 10"
                + "\nEach tag search would consume 1 credit.", creditRule.getEntityInfo());

        //Offline mode
        SearchCreditRule creditRule2 = (SearchCreditRule) offlineInputAPI.currentCreditRule();
        assertEquals(1, creditRule2.getMin());
        assertEquals(10, creditRule2.getMax());
        assertEquals(1, creditRule2.getConsumptionPerTime());
        assertEquals("The setting credit should be between 1 and 10"
                + "\nEach tag search would consume 1 credit.", creditRule2.getEntityInfo());

        //Test the credit service module
        CreditService creditService = new CreditService(new CreditStrategy(), "test");
        Entity entity = creditService.operate();
        assertTrue(entity instanceof SearchCreditRule);
        assertEquals("test", creditService.getToken());
        assertNull(creditService.getKeyword());
    }

    /**
     * Test consume search credit until running out of the credits
     */
    @Test
    public void consumeCredits(){
        //Online mode
        onlineInputAPI.setCurrentTagCredits(4);
        assertTrue(onlineInputAPI.consumeCredit());
        assertEquals(3, onlineInputAPI.getCurrentTagCredits());
        assertTrue(onlineInputAPI.consumeCredit());
        assertEquals(2, onlineInputAPI.getCurrentTagCredits());
        assertTrue(onlineInputAPI.consumeCredit());
        assertEquals(1, onlineInputAPI.getCurrentTagCredits());
        assertTrue(onlineInputAPI.consumeCredit());
        assertEquals(0, onlineInputAPI.getCurrentTagCredits());
        assertFalse(onlineInputAPI.consumeCredit());
        assertEquals(0, onlineInputAPI.getCurrentTagCredits());

        //Offline mode
        offlineInputAPI.setCurrentTagCredits(2);
        assertTrue(offlineInputAPI.consumeCredit());
        assertEquals(1, offlineInputAPI.getCurrentTagCredits());
        assertTrue(offlineInputAPI.consumeCredit());
        assertEquals(0, offlineInputAPI.getCurrentTagCredits());
        assertFalse(offlineInputAPI.consumeCredit());
        assertEquals(0, offlineInputAPI.getCurrentTagCredits());
    }

}
