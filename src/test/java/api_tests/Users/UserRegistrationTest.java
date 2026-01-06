package api_tests.Users;

import api_tests.BaseApiTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.User;
import records.UserProfile;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.*;
import static constants.ApiConstants.BASE_SCHEMA;
import static constants.ApiConstants.USER_REGISTER_SCHEMA;
import static org.junit.jupiter.api.Assertions.*;
import static constants.Messages.*;
import static utils.TestUtils.assertResponseSchema;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRegistrationTest extends BaseApiTest {

    private Stream<Arguments> validUserProfileProvider(){
        return Stream.of(
                Arguments.of(
                        "Valid User Profile",
                        new User(
                                randomUserName(),
                                randomEmail(),
                                randomPassword(8, 10)
                        )
                ),
                Arguments.of(
                        "Name Length = MIN(4)",
                        new User(
                                randomUserName(4),
                                randomEmail(),
                                randomPassword(8, 10)
                        )
                ),
                Arguments.of(
                        "Name Length = MAX(30)",
                        new User(
                                randomUserName(30),
                                randomEmail(),
                                randomPassword(8, 10)
                        )
                ),
                Arguments.of(
                        "Password Length = MIN(6)",
                        new User(
                                randomUserName(),
                                randomEmail(),
                                randomPassword(6, 7).substring(0,6)
                        )
                ),
                Arguments.of(
                        "Password Length = MAX(30)",
                        new User(
                                randomUserName(),
                                randomEmail(),
                                randomPassword(30, 31).substring(0,30)
                        )
                )
        );
    }

    @DisplayName("[API. User]. POST Method. Register user")
    @Description("""
            1. Register a new user.
            2. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
    @MethodSource("validUserProfileProvider")
    void registerUserPositiveTests(String description, User user) {
        Response response = SimpleActions.registerUser(user);

        assertResponseSchema(USER_REGISTER_SCHEMA, response);

        assertAll(description,
                () -> assertEquals(ACCOUNT_CREATED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status."),
                () -> assertEquals(user.name(), response.jsonPath().getString("data.name"), "Invalid name."),
                () -> assertEquals(user.email(), response.jsonPath().getString("data.email"), "Invalid email.")
        );
    }

    @DisplayName("[API. User]. POST Method. Register user with existing email")
    @Description("""
            1. Register a new user.
            2. Register one more user with the same email address.
            3. Assert the response.
            """)
    @Test
    void registerUserWithExistingEmailTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("New user registration validation",
                () -> assertEquals(ACCOUNT_CREATED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(201, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertNotNull(response.jsonPath().getString("data.id"), "User id is set to NULL."),
                () -> assertNotEquals("", response.jsonPath().getString("data.id"), "User id is set to empty string."),
                () -> assertEquals(user.name(), response.jsonPath().getString("data.name"), "Invalid user name."),
                () -> assertEquals(user.email(), response.jsonPath().getString("data.email"), "Invalid user email.")
        );

        Response secondResponse = SimpleActions.registerUser(user);

        assertResponseSchema(BASE_SCHEMA, secondResponse);

        assertAll("Registration with existing email is failed",
                () -> assertEquals(UNIQUE_EMAIL_REQUIRED, secondResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(409, secondResponse.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(secondResponse.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. Register user without email")
    @Description("""
            1. Register a new user without email address.
            2. Assert the response.
            """)
    @Test
    void registerUserWithoutEmailTest() {
        User user = new User(
                "Test User",
                null,
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration without email is failed",
                () -> assertEquals(VALID_EMAIL_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. Register user with empty email")
    @Description("""
            1. Register a new user with empty email address.
            2. Assert the response.
            """)
    @Test
    void registerUserWithEmptyEmailTest() {
        User user = new User(
                "Test User",
                " ",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration with empty or blank email is failed",
                () -> assertEquals(VALID_EMAIL_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. Register user with invalid email")
    @Description("""
            1. Register a new user with invalid email address.
            2. Assert the response.
            """)
    @Test
    void registerUserWithInvalidEmailTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration with invalid email is failed",
                () -> assertEquals(VALID_EMAIL_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. Register user without Name")
    @Description("""
            1. Register a new user without name.
            2. Assert the response.
            """)
    @Test
    void registerUserWithoutNameTest() {
        User user = new User(
                null,
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration without name is failed",
                () -> assertEquals(VALID_USERNAME_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. Register user with empty Name")
    @Description("""
            1. Register a new user with empty name.
            2. Assert the response.
            """)
    @Test
    void registerUserWithEmptyNameTest() {
        User user = new User(
                " ",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration with empty or blank name is failed",
                () -> assertEquals(VALID_USERNAME_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. Register user with invalid Name length")
    @Description("""
            1. Register a new user with name length more than MAX(30).
            2. Assert the response.
            """)
    @Test
    void registerUserWithInvalidNameTest() {
        User user = new User(
                "NameLength is more than MAX(30)",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration with invalid name is failed",
                () -> assertEquals(VALID_USERNAME_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. Register user without password")
    @Description("""
            1. Register a new user without password.
            2. Assert the response.
            """)
    @Test
    void registerUserWithoutPasswordTest() {
        User user = new User(
                "User Name",
                System.currentTimeMillis() + "@mail.com",
                null
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration without password is failed",
                () -> assertEquals(VALID_PASSWORD_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @DisplayName("[API. User]. POST Method. Register user with invalid password")
    @Description("""
            1. Register a new user with invalid password.
            2. Assert the response.
            """)
    @Test
    void registerUserWithInvalidPasswordTest() {
        User user = new User(
                "User Name",
                System.currentTimeMillis() + "@mail.com",
                "Short"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration without password is failed",
                () -> assertEquals(VALID_PASSWORD_REQUIRED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
