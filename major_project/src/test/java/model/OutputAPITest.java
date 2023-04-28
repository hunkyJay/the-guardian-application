package model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.entity.*;
import project.model.outputAPI.facade.OfflineOutputAPI;
import project.model.outputAPI.facade.OnlineOutputAPI;
import project.model.outputAPI.facade.OutputAPI;
import project.model.outputAPI.outputService.OptionalOutputLoginService;
import project.model.outputAPI.outputService.outputStrategy.OptionalOutputLoginStrategy;
import project.model.util.HttpHelper;
import project.model.util.QRCodeHelper;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;

/**
 * The junit tests for output api model
 */
public class OutputAPITest {
    private OutputAPI onlineOutputAPI;
    private OutputAPI offlineOutputAPI;

    @BeforeEach
    void setUp() {
        onlineOutputAPI = new OnlineOutputAPI();
        offlineOutputAPI = new OfflineOutputAPI();
    }

    /**
     * Test output report to the imgur api in online mode
     */
    @Test
    public void onlineOutputReport(){
        assertTrue(onlineOutputAPI.isOnline());
        String uri = "https://api.imgur.com/3/image";
        String outputKey = "5cf7cba24cf4fb2";
        String authorization = "Client-ID " + outputKey;
        String response = "{\"data\":{\"id\":\"uzCXSrm\",\"title\":null,\"description\":null,\"datetime\":1651571444,\"type\":\"image/jpeg\",\"animated\":false,\"width\":300,\"height\":300,\"size\":36688,\"views\":0,\"bandwidth\":0,\"vote\":null,\"favorite\":false,\"nsfw\":null,\"section\":null,\"account_url\":null,\"account_id\":0,\"is_ad\":false,\"in_most_viral\":false,\"has_sound\":false,\"tags\":[],\"ad_type\":0,\"ad_url\":\"\",\"edited\":\"0\",\"in_gallery\":false,\"deletehash\":\"36MDxK1unJfbi2b\",\"name\":\"\",\"link\":\"https://i.imgur.com/uzCXSrm.jpg\"},\"success\":true,\"status\":200}";
        JsonObject outputResponse = (JsonObject) JsonParser.parseString(response);

        //The report to sent
        Tag selectedTag = new Tag("Tag Id", "Web Url");
        Content selectedContent = new Content("id","Web Title", "Publication Date", "Web Url");
        ShortFormResult shortFormResult = new ShortFormResult(selectedTag, selectedContent);
        String shortFormReport = shortFormResult.getEntityInfo();

        //Create short form report QR code
        Path QRCodePath = Paths.get("src/main/resources/project/image/QRCode.jpg");
        String encodedQRCode = QRCodeHelper.generateQRCodeImage(shortFormReport, 300, QRCodePath);


        //Output successfully
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(uri, authorization, encodedQRCode)).thenReturn(outputResponse);

            Entity outputEntity = onlineOutputAPI.outputReport(outputKey, encodedQRCode);
            assertTrue(outputEntity instanceof Output);
            Output output = (Output) outputEntity;
            assertEquals("uzCXSrm", output.getId());
            assertEquals("image/jpeg", output.getType());
            assertEquals("36MDxK1unJfbi2b", output.getDeletehash());
            assertEquals("https://i.imgur.com/uzCXSrm.jpg", output.getLink());
            assertEquals("Id: uzCXSrm" +
                    "\nType: image/jpeg" +
                    "\nDelete hash: 36MDxK1unJfbi2b" +
                    "\nLink: https://i.imgur.com/uzCXSrm.jpg" + "\n", output.getEntityInfo());
            Output outputTest = new Output(output.getId(),output.getType(),output.getDeletehash(),output.getLink());
        }


        //Output null
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(uri, authorization, encodedQRCode)).thenReturn(null);

            Entity outputEntity = onlineOutputAPI.outputReport(outputKey, encodedQRCode);
            assertNull(outputEntity);
        }
    }

    /**
     * Test output report to the imgur api in offline mode
     */
    @Test
    public void offlineOutputReport(){
        assertFalse(offlineOutputAPI.isOnline());
        String outputKey = "5cf7cba24cf4fb2";

        //The report to sent
        Tag selectedTag = new Tag("Tag Id", "Web Url");
        Content selectedContent = new Content("id", "Web Title", "Publication Date", "Web Url");
        ShortFormResult shortFormResult = new ShortFormResult(selectedTag, selectedContent);
        String shortFormReport = shortFormResult.getEntityInfo();

        //Create short form report QR code
        Path QRCodePath = Paths.get("src/main/resources/project/image/QRCode.jpg");
        String encodedQRCode = QRCodeHelper.generateQRCodeImage(shortFormReport, 300, QRCodePath);

        //Output is null in offline mode
        Entity outputEntity = offlineOutputAPI.outputReport(outputKey, encodedQRCode);
        assertNull(outputEntity);
    }

    /**
     * Test login the reddit api
     */
    @Test
    public void loginSuccessfully(){
        assertNull(onlineOutputAPI.getAccessToken());
        assertNull(onlineOutputAPI.getUsername());
        JsonObject successfulLoginResponse = (JsonObject) JsonParser.parseString("{\"access_token\": \"1306699233613-CAMsBALre5Kp2llABxKRhsQ8ZX2NiQ\", \"token_type\": \"bearer\", \"expires_in\": 86400, \"scope\": \"*\"}");

        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(anyString(), anyString(), anyString())).thenReturn(successfulLoginResponse);
            Entity userEntity = onlineOutputAPI.login("username", "password");
            User user = (User) userEntity;
            assertEquals("username", user.getStatus());
            assertEquals("developer", user.getUserTier());
            assertEquals("1306699233613-CAMsBALre5Kp2llABxKRhsQ8ZX2NiQ", user.getToken());
            assertEquals("1306699233613-CAMsBALre5Kp2llABxKRhsQ8ZX2NiQ", onlineOutputAPI.getAccessToken());
            assertEquals("username", onlineOutputAPI.getUsername());
        }
    }

    /**
     * Test failed login the reddit api
     */
    @Test
    public void loginFailed(){
        //Online mode
        JsonObject failedLoginResponse = (JsonObject) JsonParser.parseString("{\"message\": \"Unauthorized\", \"error\": 401}");
        //Return error
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(anyString(), anyString(), anyString())).thenReturn(failedLoginResponse);
            Entity userEntity = onlineOutputAPI.login("username", "password");
            assertNull(userEntity);
            assertNull(onlineOutputAPI.getUsername());
            assertNull(onlineOutputAPI.getAccessToken());
        }
        //Error
        JsonObject errorObj = (JsonObject) JsonParser.parseString("{\"error\": error}");
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(anyString(), anyString(), anyString())).thenReturn(errorObj);
            Entity userEntity = onlineOutputAPI.login("username", "password");
            assertNull(userEntity);
            assertNull(onlineOutputAPI.getUsername());
            assertNull(onlineOutputAPI.getAccessToken());
        }


        //Return null
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(anyString(), anyString(), anyString())).thenReturn(null);
            Entity userEntity = onlineOutputAPI.login("username", "password");
            assertNull(userEntity);
            assertNull(onlineOutputAPI.getUsername());
            assertNull(onlineOutputAPI.getAccessToken());
        }

        //Offline mode
        assertNull(offlineOutputAPI.login("username", "password"));
        assertNull(offlineOutputAPI.getUsername());
        assertNull(offlineOutputAPI.getAccessToken());

        //Additionally tests
        OptionalOutputLoginService service = new OptionalOutputLoginService("username","password", new OptionalOutputLoginStrategy());
        assertNull(service.getToken());
        assertNull(service.getOutputContent());
    }

    /**
     * Test optional output to the reddit api
     */
    @Test
    public void optionalOutputReport(){
        //Online mode
        //Output successfully
        JsonObject successfulOutputResponse = (JsonObject) JsonParser.parseString("{\"jquery\": [[0, 1, \"call\", [\"body\"]], [1, 2, \"attr\", \"find\"], [2, 3, \"call\", [\".status\"]], [3, 4, \"attr\", \"hide\"], [4, 5, \"call\", []], [5, 6, \"attr\", \"html\"], [6, 7, \"call\", [\"\"]], [7, 8, \"attr\", \"end\"], [8, 9, \"call\", []], [1, 10, \"attr\", \"redirect\"], [10, 11, \"call\", [\"https://www.reddit.com/r/test/comments/ur6b67/short_form_report/\"]], [1, 12, \"attr\", \"find\"], [12, 13, \"call\", [\"*[name=url]\"]], [13, 14, \"attr\", \"val\"], [14, 15, \"call\", [\"\"]], [15, 16, \"attr\", \"end\"], [16, 17, \"call\", []], [1, 18, \"attr\", \"find\"], [18, 19, \"call\", [\"*[name=text]\"]], [19, 20, \"attr\", \"val\"], [20, 21, \"call\", [\"\"]], [21, 22, \"attr\", \"end\"], [22, 23, \"call\", []], [1, 24, \"attr\", \"find\"], [24, 25, \"call\", [\"*[name=title]\"]], [25, 26, \"attr\", \"val\"], [26, 27, \"call\", [\" \"]], [27, 28, \"attr\", \"end\"], [28, 29, \"call\", []]], \"success\": true}");
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(anyString(), anyString(), anyString())).thenReturn(successfulOutputResponse);

            Entity outputEntity = onlineOutputAPI.optionalOutputReport("username", "token", "report");
            Output output = (Output) outputEntity;
            assertEquals("optional output", output.getType());
            assertEquals("https://www.reddit.com/r/test/comments/ur6b67/short_form_report/", output.getLink());
        }

        //Output failed
        JsonObject failedOutputResponse = (JsonObject) JsonParser.parseString("{\"jquery\": [[0, 1, \"call\", [\"body\"]], [1, 2, \"attr\", \"find\"], [2, 3, \"call\", [\".status\"]], [3, 4, \"attr\", \"hide\"], [4, 5, \"call\", []], [5, 6, \"attr\", \"html\"], [6, 7, \"call\", [\"\"]], [7, 8, \"attr\", \"end\"], [8, 9, \"call\", []], [1, 10, \"attr\", \"redirect\"], [10, 11, \"call\", []], [1, 12, \"attr\", \"find\"], [12, 13, \"call\", [\"*[name=url]\"]], [13, 14, \"attr\", \"val\"], [14, 15, \"call\", [\"\"]], [15, 16, \"attr\", \"end\"], [16, 17, \"call\", []], [1, 18, \"attr\", \"find\"], [18, 19, \"call\", [\"*[name=text]\"]], [19, 20, \"attr\", \"val\"], [20, 21, \"call\", [\"\"]], [21, 22, \"attr\", \"end\"], [22, 23, \"call\", []], [1, 24, \"attr\", \"find\"], [24, 25, \"call\", [\"*[name=title]\"]], [25, 26, \"attr\", \"val\"], [26, 27, \"call\", [\" \"]], [27, 28, \"attr\", \"end\"], [28, 29, \"call\", []]], \"success\": false}");
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(anyString(), anyString(), anyString())).thenReturn(failedOutputResponse);

            Entity outputEntity = onlineOutputAPI.optionalOutputReport("username", "token", "report");
            assertNull(outputEntity);
        }

        //Test null
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(anyString(), anyString(), anyString())).thenReturn(null);

            Entity outputEntity = onlineOutputAPI.optionalOutputReport("username", "token", "report");
            assertNull(outputEntity);
        }

        //Error
        JsonObject errorObj = (JsonObject) JsonParser.parseString("{\"error\": error}");
        try (MockedStatic<HttpHelper> httpMock = mockStatic(HttpHelper.class)) {
            httpMock.when(() -> HttpHelper.postRequestWithAuthorization(anyString(), anyString(), anyString())).thenReturn(errorObj);

            Entity outputEntity = onlineOutputAPI.optionalOutputReport("username", "token", "report");
            assertNull(outputEntity);
        }

        //Offline mode
        assertNull(offlineOutputAPI.optionalOutputReport("username", "token", "report"));
    }


}
