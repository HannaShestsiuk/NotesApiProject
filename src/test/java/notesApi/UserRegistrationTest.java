package notesApi;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import records.User;
import requests.BaseApiTest;
import requests.SimpleActions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

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
                () -> assertEquals(response.jsonPath().getString("message"), "User account created successfully"),
                () -> assertEquals(response.jsonPath().getInt("status"), 201),
                () -> assertThat(response.jsonPath().getString("data.id"), is(not(emptyOrNullString()))),
                () -> assertEquals(response.jsonPath().getString("data.name"), user.name()),
                () -> assertEquals(response.jsonPath().getString("data.email"), user.email())
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
                () -> assertEquals(response.jsonPath().getString("message"), "User account created successfully"),
                () -> assertEquals(response.jsonPath().getInt("status"), 201),
                () -> assertThat(response.jsonPath().getString("data.id"), is(not(emptyOrNullString()))),
                () -> assertEquals(response.jsonPath().getString("data.name"), user.name()),
                () -> assertEquals(response.jsonPath().getString("data.email"), user.email())
        );

        Response secondResponse = SimpleActions.registerUser(user);

        assertAll("Registration with existing email is failed",
                () -> assertEquals(secondResponse.jsonPath().getString("message"),"An account already exists with the same email address"),
                () -> assertEquals(secondResponse.jsonPath().getInt("status"),409),
                () -> assertEquals(secondResponse.jsonPath().getBoolean("success"),false)
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
                () -> assertEquals(response.jsonPath().getString("message"),"A valid email address is required"),
                () -> assertEquals(response.jsonPath().getInt("status"),400),
                () -> assertEquals(response.jsonPath().getBoolean("success"),false)
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
                () -> assertEquals(response.jsonPath().getString("message"),"A valid email address is required"),
                () -> assertEquals(response.jsonPath().getInt("status"),400),
                () -> assertEquals(response.jsonPath().getBoolean("success"),false)
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
                () -> assertEquals(response.jsonPath().getString("message"),"A valid email address is required"),
                () -> assertEquals(response.jsonPath().getInt("status"),400),
                () -> assertEquals(response.jsonPath().getBoolean("success"),false)
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
                () -> assertThat(response.jsonPath().getString("message"), is("User name must be between 4 and 30 characters")),
                () -> assertThat(response.jsonPath().getInt("status"), is(400)),
                () -> assertThat(response.jsonPath().getBoolean("success"), is(false))
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
                () -> assertThat(response.jsonPath().getString("message"), is("User name must be between 4 and 30 characters")),
                () -> assertThat(response.jsonPath().getInt("status"), is(400)),
                () -> assertThat(response.jsonPath().getBoolean("success"), is(false))
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
                () -> assertThat(response.jsonPath().getString("message"), is("User name must be between 4 and 30 characters")),
                () -> assertThat(response.jsonPath().getInt("status"), is(400)),
                () -> assertThat(response.jsonPath().getBoolean("success"), is(false))
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
                () -> assertThat(response.jsonPath().getString("message"), is("Password must be between 6 and 30 characters")),
                () -> assertThat(response.jsonPath().getInt("status"), is(400)),
                () -> assertThat(response.jsonPath().getBoolean("success"), is(false))
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
                () -> assertThat(response.jsonPath().getString("message"), is("Password must be between 6 and 30 characters")),
                () -> assertThat(response.jsonPath().getInt("status"), is(400)),
                () -> assertThat(response.jsonPath().getBoolean("success"), is(false))
        );
    }
}
