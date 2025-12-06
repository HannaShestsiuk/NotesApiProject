package api_tests.Notes;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import records.UserLogin;
import requests.SimpleActions;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.http.ContentType.URLENC;

public class BaseApiTest {
    public static String authToken = "";
    public static String userId = "";

    @BeforeAll
    public static void authUser(){
        RestAssured.config = RestAssured.config()
                .encoderConfig(encoderConfig()
                        .defaultContentCharset("UTF-8")
                        .encodeContentTypeAs("application/x-www-form-urlencoded", URLENC));

        UserLogin userLogin = new UserLogin("automation@mail.com", "password1");

        Response response = SimpleActions.loginUser(userLogin);

        authToken = response.jsonPath().getString("data.token");
        userId    = response.jsonPath().getString("data.id");
    }
}
