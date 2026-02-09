package api_tests.users;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.UserLogin;
import records.User;
import requests.SimpleActions;

import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserLoginTest {

    @DisplayName("[API. User]. POST Method. User login")
    @Description("""
            1. Register a new user.
            2. Assert the response.
            3. Login user.
            4. Assert the response.
            """)
    @Test
    void loginRegisteredUserTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(ACCOUNT_CREATED, registerUserResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"), "Invalid Status Code.")
        );

        String userId = registerUserResponse.jsonPath().getString("data.id");

        UserLogin userLogin = new UserLogin(
                user.email(),
                user.password()
        );

        Response response = SimpleActions.loginUser(userLogin);

        assertAll("Registered user is logged in",
                () -> assertEquals(LOGIN_SUCCESS, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status."),
                () -> assertEquals(userLogin.email(), response.jsonPath().getString("data.email"), "Invalid user userName."),
                () -> assertEquals(userId, response.jsonPath().getString("data.id"), "Invalid user id.")
       );
    }

    @DisplayName("[API. User]. POST Method. User login with Invalid password")
    @Description("""
            1. Register a new user.
            2. Assert the response.
            3. Login user with invalid password.
            4. Assert the response.
            """)
    @Test
    void loginWithInvalidPasswordTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(ACCOUNT_CREATED, registerUserResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"), "Invalid Status Code.")
        );

        UserLogin userLogin = new UserLogin(
                user.email(),
                user.password() + "invalid"
        );

        Response response = SimpleActions.loginUser(userLogin);

        assertAll("Failed login with invalid password",
                () -> assertEquals(LOGIN_INVALID_EMAIL_OR_PASSWORD, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. User login with Empty password")
    @Description("""
            1. Register a new user.
            2. Assert the response.
            3. Login user with empty password.
            4. Assert the response.
            """)
    @Test
    void loginWithEmptyPasswordTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(ACCOUNT_CREATED, registerUserResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"), "Invalid Status Code.")
        );

        UserLogin userLogin = new UserLogin(
                user.email(),
                ""
        );

        Response response = SimpleActions.loginUser(userLogin);

        assertAll("Failed login with empty password",
                () -> assertEquals(VALID_PASSWORD_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. User login without password")
    @Description("""
            1. Register a new user.
            2. Assert the response.
            3. Login user without password.
            4. Assert the response.
            """)
    @Test
    void loginWithoutPasswordTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(ACCOUNT_CREATED, registerUserResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"), "Invalid Status Code.")
        );

        UserLogin userLogin = new UserLogin(
                user.email(),
                null
        );

        Response response = SimpleActions.loginUser(userLogin);

        assertAll("Failed login without password",
                () -> assertEquals(VALID_PASSWORD_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. User login with non-registered email")
    @Description("""
            1. Register a new user.
            2. Assert the response.
            3. Login user with non-registered email.
            4. Assert the response.
            """)
    @Test
    void loginWithNonRegisteredEmailTest() {
        UserLogin userLogin = new UserLogin(
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.loginUser(userLogin);

        assertAll("Failed login with NON registered email",
                () -> assertEquals(LOGIN_INVALID_EMAIL_OR_PASSWORD, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. User login with Invalid email")
    @Description("""
            1. Register a new user.
            2. Assert the response.
            3. Login user with invalid email.
            4. Assert the response.
            """)
    @Test
    void loginWithInvalidEmailTest() {
        UserLogin userLogin = new UserLogin(
                System.currentTimeMillis() + "mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.loginUser(userLogin);

        assertAll("Failed login with invalid email",
                () -> assertEquals(VALID_EMAIL_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
