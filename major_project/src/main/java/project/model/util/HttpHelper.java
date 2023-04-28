package project.model.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * An util class to help make get, post, or put http requests
 */
public class HttpHelper {
    //Java11+ new features learned from lecture to make http requests

    /**
     * Make a http get request and get the response json object
     * @param uri the uri the make http requests
     * @return a http response json object
     */
    public static JsonObject getRequest(String uri) {
        JsonObject result = null;
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int responseCode = response.statusCode();
            System.out.println("Response Code: " + responseCode);//HTTP_OK = 200, Http_CREATED = 201
            if ((responseCode == HttpURLConnection.HTTP_CREATED) || (responseCode == HttpURLConnection.HTTP_OK)) {
                result = (JsonObject) JsonParser.parseString(response.body());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
            return null;
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
            return null;
        }

        return result;
    }

    /**
     * Make a http get request and get the response json object
     * @param uri the uri the make http requests
     * @param authorization the authorization string put in the http header
     * @return a http response json object
     */
    public static JsonObject postRequestWithAuthorization(String uri, String authorization, String post){
        JsonObject result = null;
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .POST(HttpRequest.BodyPublishers.ofString(post))
                    .header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8")
                    .header("Authorization", authorization)
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int responseCode = response.statusCode();
            System.out.println("Response Code: " + responseCode);//HTTP_OK = 200, Http_CREATED = 201
            if ((responseCode == HttpURLConnection.HTTP_CREATED) || (responseCode == HttpURLConnection.HTTP_OK)) {
                result = (JsonObject) JsonParser.parseString(response.body());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
            return null;
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
            return null;
        }

        return result;
    }



}
