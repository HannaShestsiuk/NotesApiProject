package requests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.AllureUtils;

import static constants.Constants.BASE_URL_NOTES;

public class RequestLibrary {
    @Step("Sending GET request on {url}")
    public static Response sendGetRequest(String url, String authToken){
        Response response = RestAssured.given()
                .header("accept", "application/json")
                .header("x-auth-token", authToken)
                .log().all()
                .when()
                .get(BASE_URL_NOTES + url)
                .then()
                .log().body()
                .extract()
                .response();

        AllureUtils.attachResponseToAllure(response);
        return response;
    }

    @Step("Sending GET request on {url}")
    public static Response sendGetRequestWithParam(String url, String authToken, String parameter){
        Response response = RestAssured.given()
                .header("accept", "application/json")
                .header("x-auth-token", authToken)
                .log().all()
                .when()
                .get(BASE_URL_NOTES + url + parameter)
                .then()
                .log().body()
                .extract()
                .response();

        AllureUtils.attachResponseToAllure(response);
        return response;
    }

    @Step("Sending POST request on {url}")
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
                .log().all()
                .when()
                .post(BASE_URL_NOTES + url)
                .then()
                .log().body()
                .extract()
                .response();

        AllureUtils.attachResponseToAllure(response);
        return response;
    }

    @Step("Sending POST request on {url}")
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
                .post(BASE_URL_NOTES + url)
                .then()
                .log().body()
                .extract()
                .response();

        AllureUtils.attachResponseToAllure(response);
        return response;
    }

    @Step("Sending PUT request on {url}")
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
                .put(BASE_URL_NOTES + url + parameter)
                .then()
                .log().body()
                .extract()
                .response();

        AllureUtils.attachResponseToAllure(response);
        return response;
    }

    @Step("Sending PATCH request on {url}")
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
                .patch(BASE_URL_NOTES + url)
                .then()
                .log().body()
                .extract()
                .response();

        AllureUtils.attachResponseToAllure(response);
        return response;
    }

    @Step("Sending PATCH request on {url}")
    public static Response sendPatchRequestWithParam(Record record, String url, String parameter, String authToken){
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
                .patch(BASE_URL_NOTES + url + parameter)
                .then()
                .log().body()
                .extract()
                .response();

        AllureUtils.attachResponseToAllure(response);
        return response;
    }

    @Step("Sending DELETE request on {url}")
    public static Response sendDeleteRequest(String url, String authToken){
        Response response = RestAssured.given()
                .header("accept", "application/json")
                .header("x-auth-token", authToken)
                .log().all()
                .when()
                .delete(BASE_URL_NOTES + url)
                .then()
                .log().body()
                .extract()
                .response();

        AllureUtils.attachResponseToAllure(response);
        return response;
    }

    @Step("Sending DELETE request on {url}")
    public static Response sendDeleteRequestWithParam(String url, String parameter, String authToken){
        Response response = RestAssured.given()
                .header("accept", "application/json")
                .header("x-auth-token", authToken)
                .log().all()
                .when()
                .delete(BASE_URL_NOTES + url + parameter)
                .then()
                .log().body()
                .extract()
                .response();

        AllureUtils.attachResponseToAllure(response);
        return response;
    }
}
