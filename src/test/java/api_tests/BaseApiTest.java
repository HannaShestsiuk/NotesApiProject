package api_tests;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import records.User;
import records.UserLogin;
import requests.SimpleActions;

import static classes.TestDataGenerator.*;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.http.ContentType.URLENC;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseApiTest {
    public static String authToken = "";
    public static String userId = "";
    public static String userName = "";
    public static String userEmail = "";
    public static String userPassword = "";
    public static String userPhone = "";
    public static String userCompany = "";

    @BeforeAll
    public static void authUser(){
        RestAssured.config = RestAssured.config()
                .encoderConfig(encoderConfig()
                        .defaultContentCharset("UTF-8")
                        .encodeContentTypeAs("application/x-www-form-urlencoded", URLENC));

        userName = randomUserName();
        userEmail = randomEmail();
        userPassword = randomPassword(6,7);
        User user = new User(userName, userEmail, userPassword);

        Response registerUserResponse = SimpleActions.registerUser(user);
        assertEquals(201, registerUserResponse.statusCode(), "User registration failed");

        UserLogin userLogin = new UserLogin(userEmail, userPassword);

        Response userLoginResponse = SimpleActions.loginUser(userLogin);
        assertEquals(200, userLoginResponse.statusCode(), "User login failed");

        authToken = userLoginResponse.jsonPath().getString("data.token");
        userId    = userLoginResponse.jsonPath().getString("data.id");
    }
}
