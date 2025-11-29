package api_tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import classes.LoginUser;
import records.RegisteredUser;
import records.User;
import requests.SimpleActions;

import static enums.Messages.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserLoginTest extends BaseApiTest {
    @Test
    void loginRegisteredUserTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(ACCOUNT_CREATED.getLabel(), registerUserResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"), "Invalid Status Code.")
        );

        String userId = registerUserResponse.jsonPath().getString("data.id");

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                user.password()
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Registered user is logged in",
                () -> assertEquals(LOGIN_SUCCESS.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status."),
                () -> assertEquals(registeredUser.email(), response.jsonPath().getString("data.email"), "Invalid user name."),
                () -> assertEquals(userId, response.jsonPath().getString("data.id"), "Invalid user id.")
       );
    }

    @Test
    void loginWithInvalidPasswordTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(ACCOUNT_CREATED.getLabel(), registerUserResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"), "Invalid Status Code.")
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                user.password() + "invalid"
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with invalid password",
                () -> assertEquals(LOGIN_INVALID_EMAIL_OR_PASSWORD.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void loginWithEmptyPasswordTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(ACCOUNT_CREATED.getLabel(), registerUserResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"), "Invalid Status Code.")
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                ""
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with empty password",
                () -> assertEquals(VALID_PASSWORD_REQUIRED.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void loginWithoutPasswordTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(ACCOUNT_CREATED.getLabel(), registerUserResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"), "Invalid Status Code.")
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                null
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login without password",
                () -> assertEquals(VALID_PASSWORD_REQUIRED.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void loginWithNonRegisteredEmailTest() {
        RegisteredUser registeredUser = new RegisteredUser(
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with NON registered email",
                () -> assertEquals(LOGIN_INVALID_EMAIL_OR_PASSWORD.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void loginWithInvalidEmailTest() {
        LoginUser registeredUser = new LoginUser(
                System.currentTimeMillis() + "mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with invalid email",
                () -> assertEquals(VALID_EMAIL_REQUIRED.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
