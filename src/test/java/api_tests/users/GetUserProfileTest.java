package api_tests.users;

import api_tests.BaseApiTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import records.User;
import records.UserLogin;
import requests.SimpleActions;

import static classes.TestDataGenerator.*;
import static constants.ApiConstants.BASE_SCHEMA;
import static constants.ApiConstants.USER_GET_PROFILE_SCHEMA;
import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.assertResponseSchema;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetUserProfileTest extends BaseApiTest {
    @DisplayName("[API. User]. GET Method. Get user's profile")
    @Description("""
            1. Get user profile.
            2. Assert the response.
            """)
    @Test
    void getUserProfile() {

        // Create isolated local user
        String name = randomUserName();
        String email = randomEmail();
        String password = randomPassword(6,7);
        User user = new User(name, email, password);

        Response registerUser = SimpleActions.registerUser(user);
        assertEquals(201, registerUser.statusCode(), "User registration failed");

        Response loginUser = SimpleActions.loginUser(new UserLogin(email, password));
        assertEquals(200, loginUser.statusCode(), "User login failed");

        String token = loginUser.jsonPath().getString("data.token");
        String userId = loginUser.jsonPath().getString("data.id");

        Response response = SimpleActions.getUserProfile(token);

        assertResponseSchema(USER_GET_PROFILE_SCHEMA, response);

        assertAll("Retrieve user profile",
                () -> assertEquals(USER_PROFILE, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status."),
                () -> assertEquals(email, response.jsonPath().getString("data.email"), "Invalid user name."),
                () -> assertEquals(userId, response.jsonPath().getString("data.id"), "Invalid user id.")
        );

        registerLoggedInUser(token);
    }

    @DisplayName("[API. User]. GET Method. Get user's profile without auth")
    @Description("""
            1. Get user profile.
            2. Assert the response.
            """)
    @Test
    void getUnauthorizedUserProfile() {

        Response response = SimpleActions.getUserProfile("");

        assertResponseSchema(BASE_SCHEMA, response);

        assertAll("Retrieve user profile",
                () -> assertEquals(NO_AUTH_HEADER, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
