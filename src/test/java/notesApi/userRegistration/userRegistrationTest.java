package notesApi.userRegistration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import notesApi.ApiConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class userRegistrationTest {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ApiConstants.BASE_URI;
    }

    @Test
    public void registerUserWithValidData() {
        String name = "Test User";
        String email = "user" + System.currentTimeMillis() + "@mail.com";
        String password = "Strong123!";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("email", email);
        requestBody.put("password", password);

//        String body = String.format(
//                "{ \"name\": \"%s\", \"email\": \"%s\", \"password\": \"%s\" }",
//                name, email, password
//        );

        Response response = RestAssured
                .given()
                .accept("application/json")
                .contentType("application/x-www-form-urlencoded")
                //.body(requestBody)
                // or
                //.formParam("name", name) bla-bla-bla
                // or
                //.param("name", name) bla-bla-bla
                .when()
                .post("/users/register")
                .then()
                .extract()
                .response();

        //System.out.println(response.asString());

//        assertAll("Health-check response validation",
//                () -> assertThat(response.jsonPath().getString("message"), is("User account created successfully")),
//                () -> assertThat(response.jsonPath().getInt("status"), is(201)),
//                () -> assertThat(response.jsonPath().getString("data.id"), notNullValue()), // how to check it is not empty??
//                () -> assertThat(response.jsonPath().getString("data.name"), is(name)),
//                () -> assertThat(response.jsonPath().getString("data.email"), is(email))
//        );
    }
}
