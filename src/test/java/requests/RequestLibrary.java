package requests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static constants.ApiConstants.BASE_URI;

public class RequestLibrary {
    public static Response sendGetRequest(String url, String authToken){
        Response response = RestAssured.given()
                .header("accept", "application/json")
                .header("x-auth-token", authToken)
                .log().all()
                .when()
                .get(BASE_URI + url)
                .then()
                .log().body()
                .extract()
                .response();

        return response;
    }

    public static Response sendGetRequestWithParam(String url, String authToken, String parameter){
        Response response = RestAssured.given()
                .header("accept", "application/json")
                .header("x-auth-token", authToken)
                .log().all()
                .when()
                .get(BASE_URI + url + parameter)
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

    public static Response sendPostRequest(Record record, String url, String authToken){
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


    public static Response sendPutRequestWithParam(Record record, String url, String parameter, String authToken){
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
                .put(BASE_URI + url + parameter)
                .then()
                .log().body()
                .extract()
                .response();

        return response;
    }

    public static Response sendPatchRequest(Record record, String url, String authToken){
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
                .patch(BASE_URI + url)
                .then()
                .log().body()
                .extract()
                .response();

        return response;
    }

    public static Response sendDeleteRequest(String url, String authToken){
        Response response = RestAssured.given()
                .header("accept", "application/json")
                .header("x-auth-token", authToken)
                .log().all()
                .when()
                .delete(BASE_URI + url)
                .then()
                .log().body()
                .extract()
                .response();

        return response;
    }
}
