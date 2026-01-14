package api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import records.User;
import records.UserLogin;
import requests.SimpleActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static classes.TestDataGenerator.*;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.http.ContentType.URLENC;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseApiTest {

    // Manage if a global user should be created per test class
    protected boolean enableGlobalUser = false;

    protected String authToken;
    protected String userId;
    protected String userName;
    protected String userEmail;
    protected String userPassword;

    protected List<String> createdNoteIds = new ArrayList<>();


    @BeforeAll
    void authUser(){

        if (!enableGlobalUser) {
            return;
        }

        // Create a global user
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

    protected void registerNoteId(Response response) {
        try {
            String noteId = response.jsonPath().getString("data.id");
            if (noteId != null) {
                createdNoteIds.add(noteId);
            }
        } catch (Exception ignored) {}
    }

    @AfterAll
    void deleteUser() {

//        if (!enableGlobalUser) {
//            return;
//        }

        // Delete all created notes
        for (String noteId : createdNoteIds) {
            try {
                SimpleActions.deleteNote(noteId, authToken);
            } catch (Exception e) {
                System.out.println("Failed to delete note " + noteId + ": " + e.getMessage());
            }
        }
        createdNoteIds.clear();

        // Delete the global user
        try {
            UserLogin userLogin = new UserLogin(userEmail, userPassword);
            Response loginResponse = SimpleActions.loginUser(userLogin);

            if (loginResponse.statusCode() == 200) {
                String freshToken = loginResponse.jsonPath().getString("data.token");
                SimpleActions.deleteAccount(freshToken);
            }
        } catch (Exception e) {
            System.out.println("User deletion warning: " + e.getMessage());
        }

    userName = null;
    userEmail = null;
    userPassword = null;
    authToken = null;
    userId = null;
    }
}
