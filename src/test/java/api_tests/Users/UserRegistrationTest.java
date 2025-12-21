package api_tests.Users;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.provider.Arguments;
import records.User;
import records.UserProfile;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static constants.Messages.*;

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

        assertAll("Registration with existing email is failed",
                () -> assertEquals(UNIQUE_EMAIL_REQUIRED, secondResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(409, secondResponse.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(secondResponse.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

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
