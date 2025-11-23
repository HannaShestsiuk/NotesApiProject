package notesApi;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import records.RegisteredUser;
import records.User;
import requests.BaseApiTest;
import requests.SimpleActions;

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
                () -> assertEquals("User account created successfully", registerUserResponse.jsonPath().getString("message")),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"))
        );

        String userId = registerUserResponse.jsonPath().getString("data.id");

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                user.password()
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Registered user is logged in",
                () -> assertEquals("Login successful", response.jsonPath().getString("message")),
                () -> assertEquals(200, response.jsonPath().getInt("status")),
                () -> assertTrue(response.jsonPath().getBoolean("success")),
                () -> assertEquals(registeredUser.email(), response.jsonPath().getString("data.email")),
                () -> assertEquals(userId, response.jsonPath().getString("data.id"))
        );
    }

    @Test
    void shouldNotLoginWithInvalidPasswordTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals("User account created successfully", registerUserResponse.jsonPath().getString("message")),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"))
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                user.password() + "invalid"
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with invalid password",
                () -> assertEquals("Incorrect email address or password", response.jsonPath().getString("message")),
                () -> assertEquals(401, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotLoginWithEmptyPasswordTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals("User account created successfully", registerUserResponse.jsonPath().getString("message")),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"))
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                ""
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with empty password",
                () -> assertEquals("Password must be between 6 and 30 characters", response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotLoginWithoutPasswordTest() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals("User account created successfully", registerUserResponse.jsonPath().getString("message")),
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status"))
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                null
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login without password",
                () -> assertEquals("Password must be between 6 and 30 characters", response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotLoginWithNonRegisteredEmailTest() {
        RegisteredUser registeredUser = new RegisteredUser(
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with NON registered email",
                () -> assertEquals("Incorrect email address or password", response.jsonPath().getString("message")),
                () -> assertEquals(401, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @Test
    void shouldNotLoginWithInvalidEmailTest() {
        RegisteredUser registeredUser = new RegisteredUser(
                System.currentTimeMillis() + "mail.com",
                "Strong123!"
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with invalid email",
                () -> assertEquals("A valid email address is required", response.jsonPath().getString("message")),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }
}
