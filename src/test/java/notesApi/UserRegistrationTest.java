package notesApi;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import records.User;
import requests.BaseApiTest;
import requests.SimpleActions;

import static org.junit.jupiter.api.Assertions.*;
import static records.Messages.*;

public class UserRegistrationTest extends BaseApiTest {

    @Test
    void registerUserWithValidData() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(ACCOUNT_CREATED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(201, response.jsonPath().getInt("status")),
                () -> assertNotNull(response.jsonPath().getString("data.id")),
                () -> assertNotEquals("", response.jsonPath().getString("data.id")),
                () -> assertEquals(user.name(), response.jsonPath().getString("data.name")),
                () -> assertEquals(user.email(), response.jsonPath().getString("data.email"))
        );
    }

    @Test
    void shouldNotRegisterUserWithExistingEmail() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("New user registration validation",
                () -> assertEquals(ACCOUNT_CREATED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(201, response.jsonPath().getInt("status")),
                () -> assertNotNull(response.jsonPath().getString("data.id")),
                () -> assertNotEquals("", response.jsonPath().getString("data.id")),
                () -> assertEquals(user.name(), response.jsonPath().getString("data.name")),
                () -> assertEquals(user.email(), response.jsonPath().getString("data.email"))
        );

        Response secondResponse = SimpleActions.registerUser(user);

        assertAll("Registration with existing email is failed",
                () -> assertEquals(UNIQUE_EMAIL_REQUIRED.getLabel(), secondResponse.jsonPath().getString("message")),
                () -> assertEquals(409, secondResponse.jsonPath().getInt("status")),
                () -> assertFalse(secondResponse.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotRegisterUserWithoutEmail() {
        User user = new User(
                "Test User",
                null,
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration without email is failed",
                () -> assertEquals(VALID_EMAIL_REQUIRED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotRegisterUserWithEmptyOrBlankEmail() {
        User user = new User(
                "Test User",
                " ",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration with empty or blank email is failed",
                () -> assertEquals(VALID_EMAIL_REQUIRED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotRegisterUserWithInvalidEmail() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration with invalid email is failed",
                () -> assertEquals(VALID_EMAIL_REQUIRED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotRegisterUserWithoutName() {
        User user = new User(
                null,
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration without name is failed",
                () -> assertEquals(VALID_USERNAME_REQUIRED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotRegisterUserWithEmptyOrBlankName() {
        User user = new User(
                " ",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration with empty or blank name is failed",
                () -> assertEquals(VALID_USERNAME_REQUIRED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotRegisterUserWithInvalidName() {
        User user = new User(
                "NameLength is more than MAX(30)",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration with invalid name is failed",
                () -> assertEquals(VALID_USERNAME_REQUIRED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotRegisterUserWithoutPassword() {
        User user = new User(
                "User Name",
                System.currentTimeMillis() + "@mail.com",
                null
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration without password is failed",
                () -> assertEquals(VALID_PASSWORD_REQUIRED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotRegisterUserWithInvalidPassword() {
        User user = new User(
                "User Name",
                System.currentTimeMillis() + "@mail.com",
                "Short"
        );

        Response response = SimpleActions.registerUser(user);

        assertAll("Registration without password is failed",
                () -> assertEquals(VALID_PASSWORD_REQUIRED.getLabel(), response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }
}
