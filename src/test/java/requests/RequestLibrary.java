package requests;


import classes.LoginUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static constants.ApiConstants.BASE_URI;
import static io.restassured.http.ContentType.URLENC;

public class RequestLibrary {
    public static Response sendGetRequest(String url){
        Response response = RestAssured.given()
                .header("accept", "application/json")
                //.queryParams(queryParams)
                .log().all()
                .when()
                .get(BASE_URI + url)
                .then()
                .log().body()
                .extract()
                .response();

        return response;
    }

    public static Response sendPostRequest(Record record, String url){
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = null;

        try{
            jsonBody = objectMapper.writeValueAsString(record);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given()
                .body(jsonBody)
                .header("accept", "application/json")
                .contentType("application/json")
                //.header("x-auth-token", authToken)
                .log().all()
                .when()
                .post(BASE_URI + url)
                .then()
                .log().body()
                .extract()
                .response();

        return response;
    }

    public static Response sendPostRequest(LoginUser user, String url){
        Response response = RestAssured.given()
                //.header("accept", "application/json")
                .contentType(URLENC)
                .formParams(toForm(user))
                .log().all()
                .when()
                .post(BASE_URI + url)
                .then()
                .log().body()
                .extract()
                .response();

        return response;
    }

    private static Map<String, String> toForm(LoginUser user) {
        Map<String, String> form = new HashMap<>();
        form.put("email", user.getEmail());
        form.put("password", user.getPassword());
        return form;
    }

    public static Response sendPostRequestWithAuth(Record record, String url, String authToken){
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = null;

        try{
            jsonBody = objectMapper.writeValueAsString(record);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given()
                .body(jsonBody)
                .header("accept", "application/json")
                .contentType("application/json")
                .header("x-auth-token", authToken)
                .log().all()
                .when()
                .post(BASE_URI + url)
                .then()
                .log().body()
                .extract()
                .response();

        return response;
    }
}
