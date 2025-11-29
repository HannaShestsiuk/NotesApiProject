package api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import classes.LoginUser;
import records.RegisteredUser;
import requests.SimpleActions;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.http.ContentType.URLENC;

public class BaseApiTest implements SimpleActions {
    public static String authToken = "";
    public static String userId = "";

    @BeforeAll
    public static void authUser(){
        RestAssured.config = RestAssured.config()
                .encoderConfig(encoderConfig()
                        .defaultContentCharset("UTF-8")
                        .encodeContentTypeAs("application/x-www-form-urlencoded", URLENC));

        RegisteredUser user = new RegisteredUser("automation@mail.com", "password1");

        Response response = SimpleActions.loginUser(user);

        authToken = response.jsonPath().getString("data.token");
        userId    = response.jsonPath().getString("data.id");
    }
}
