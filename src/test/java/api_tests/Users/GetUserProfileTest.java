package api_tests.Users;

import api_tests.BaseApiTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import requests.SimpleActions;

import static constants.ApiConstants.BASE_SCHEMA;
import static constants.ApiConstants.USER_GET_PROFILE_SCHEMA;
import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.assertResponseSchema;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class GetUserProfileTest extends BaseApiTest {
    @DisplayName("[API. User]. GET Method. Get user's profile")
    @Description("""
            1. Get user profile.
            2. Assert the response.
            """)
    @Test
    void getUserProfile() {

        Response response = SimpleActions.getUserProfile(authToken);

        assertResponseSchema(USER_GET_PROFILE_SCHEMA, response);

        assertAll("Retrieve user profile",
                () -> assertEquals(USER_PROFILE, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status."),
                () -> assertEquals(userEmail, response.jsonPath().getString("data.email"), "Invalid user name."),
                () -> assertEquals(userId, response.jsonPath().getString("data.id"), "Invalid user id.")
        );
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
