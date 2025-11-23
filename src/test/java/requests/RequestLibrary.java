package requests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import notesApi.ApiConstants;

public class RequestLibrary {
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
                //.header("x-auth-token", authContent)
                .log().all()
                .when()
                .post(ApiConstants.BASE_URI + url)
                .then()
                .log().body()
                .extract()
                .response();

        return response;
    }
}
