package notesApi;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import records.RegisteredUser;
import records.User;
import requests.BaseApiTest;
import requests.SimpleActions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                () -> assertEquals(registerUserResponse.jsonPath().getString("message"), "User account created successfully"),
                () -> assertEquals(registerUserResponse.jsonPath().getInt("status"), 201)
        );

        String userId = registerUserResponse.jsonPath().getString("data.id");

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                user.password()
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Registered user is logged in",
                () -> assertEquals(response.jsonPath().getString("message"), "Login successful"),
                () -> assertEquals(response.jsonPath().getInt("status"), 200),
                () -> assertEquals(response.jsonPath().getBoolean("success"), true),
                () -> assertEquals(response.jsonPath().getString("data.email"), registeredUser.email()),
                () -> assertEquals(response.jsonPath().getString("data.id"), userId)
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
                () -> assertEquals(registerUserResponse.jsonPath().getString("message"), "User account created successfully"),
                () -> assertEquals(registerUserResponse.jsonPath().getInt("status"), 201)
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                user.password() + "invalid"
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with invalid password",
                () -> assertEquals(response.jsonPath().getString("message"), "Incorrect email address or password"),
                () -> assertEquals(response.jsonPath().getInt("status"), 401),
                () -> assertEquals(response.jsonPath().getBoolean("success"), false)
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
                () -> assertEquals(registerUserResponse.jsonPath().getString("message"), "User account created successfully"),
                () -> assertEquals(registerUserResponse.jsonPath().getInt("status"), 201)
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                ""
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login with empty password",
                () -> assertEquals(response.jsonPath().getString("message"), "Password must be between 6 and 30 characters"),
                () -> assertEquals(response.jsonPath().getInt("status"), 400),
                () -> assertEquals(response.jsonPath().getBoolean("success"), false)
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
                () -> assertEquals(registerUserResponse.jsonPath().getString("message"), "User account created successfully"),
                () -> assertEquals(registerUserResponse.jsonPath().getInt("status"), 201)
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                null
        );

        Response response = SimpleActions.loginUser(registeredUser);

        assertAll("Failed login without password",
                () -> assertEquals(response.jsonPath().getString("message"), "Password must be between 6 and 30 characters"),
                () -> assertEquals(response.jsonPath().getInt("status"), 400),
                () -> assertEquals(response.jsonPath().getBoolean("success"), false)
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
                () -> assertEquals(response.jsonPath().getString("message"), "Incorrect email address or password"),
                () -> assertEquals(response.jsonPath().getInt("status"), 401),
                () -> assertEquals(response.jsonPath().getBoolean("success"), false)
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
                () -> assertEquals(response.jsonPath().getString("message"), "A valid email address is required"),
                () -> assertEquals(response.jsonPath().getInt("status"), 400),
                () -> assertEquals(response.jsonPath().getBoolean("success"), false)
        );
    }
}
