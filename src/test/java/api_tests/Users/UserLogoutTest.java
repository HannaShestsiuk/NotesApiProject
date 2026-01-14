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
import static constants.Messages.USER_LOGOUT;
import static org.junit.jupiter.api.Assertions.*;
import static utils.TestUtils.assertResponseSchema;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserLogoutTest extends BaseApiTest {

    @DisplayName("[API. User]. DELETE Method. Logout user")
    @Description("""
            1. Register a new user.
            2. Login user.
            3. Logout user.
            4. Assert the response.
            """)
    @Test
    void logoutUserTest() {

        // Create isolated user
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

        Response userLogout = SimpleActions.logout(token);

        assertResponseSchema(BASE_SCHEMA, userLogout);

        assertAll("User is logged out",
                () -> assertEquals(USER_LOGOUT, userLogout.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, userLogout.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(userLogout.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
