package api_tests.Users;

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
import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.TestUtils.assertResponseSchema;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteAccountTest extends BaseApiTest {

    @DisplayName("[API. User]. DELETE Method. Delete user's account")
    @Description("""
            1. Register new user.
            2. Login user.
            3. Delete user account.
            4. Assert the response.
            5. Attempt to login as a user with deleted account.
            6. Assert the response.
            """)
    @Test
    void deleteAccountTest() {

        // Create isolated local User
        String name = randomUserName();
        String email = randomEmail();
        String password = randomPassword(6,7);
        User user = new User(name, email, password);

        Response registerUserResponse = SimpleActions.registerUser(user);
        assertEquals(201, registerUserResponse.statusCode(), "User registration failed");

        UserLogin userLogin = new UserLogin(email, password);

        Response userLoginResponse = SimpleActions.loginUser(userLogin);
        assertEquals(200, userLoginResponse.statusCode(), "User login failed");

        String token = userLoginResponse.jsonPath().getString("data.token");
        Response response = SimpleActions.deleteAccount(token);

        assertResponseSchema(BASE_SCHEMA, response);

        assertAll("Account is deleted",
                () -> assertEquals(ACCOUNT_DELETED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );

        // Attempt to login after deletion
        UserLogin deletedUserLogin = new UserLogin(email, password);

        Response deletedUserLoginResponse = SimpleActions.loginUser(deletedUserLogin);

        assertAll("Deleted user login failed",
                () -> assertEquals(LOGIN_INVALID_EMAIL_OR_PASSWORD, deletedUserLoginResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, deletedUserLoginResponse.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(deletedUserLoginResponse.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. DELETE Method. Delete user's account without login")
    @Description("""
            1. Register new user.
            2. Delete user account without login.
            3. Assert the response.
            4. Login user.
            5. Assert the response.
            """)
    @Test
    void deleteAccountByNonRegisteredUserTest() {

        // Create isolated local User
        String name = randomUserName();
        String email = randomEmail();
        String password = randomPassword(6,7);
        User user = new User(name, email, password);

        Response registerUserResponse = SimpleActions.registerUser(user);
        assertEquals(201, registerUserResponse.statusCode(), "User registration failed");

        Response response = SimpleActions.deleteAccount("");

        assertResponseSchema(BASE_SCHEMA, response);

        assertAll("Account is NOT deleted by non-registered user",
                () -> assertEquals(NO_AUTH_HEADER, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );

        UserLogin existingUserLogin = new UserLogin(email, password);

        Response existingUserLoginResponse = SimpleActions.loginUser(existingUserLogin);

        assertAll("User is logged in",
                () -> assertEquals(LOGIN_SUCCESS, existingUserLoginResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, existingUserLoginResponse.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(existingUserLoginResponse.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
