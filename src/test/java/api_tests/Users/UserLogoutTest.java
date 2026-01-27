package api_tests.Users;

import api_tests.BaseApiTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.User;
import records.UserLogin;
import requests.SimpleActions;

import static helpers.TestDataGenerator.*;
import static constants.Messages.USER_LOGOUT;
import static org.junit.jupiter.api.Assertions.*;

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

        userName = randomUserName();
        userEmail = randomEmail();
        userPassword = randomPassword(6,7);
        User user = new User(userName, userEmail, userPassword);

        Response registerUserResponse = SimpleActions.registerUser(user);
        assertEquals(201, registerUserResponse.statusCode(), "User registration failed");

        UserLogin userLogin = new UserLogin(userEmail, userPassword);

        Response userLoginResponse = SimpleActions.loginUser(userLogin);
        assertEquals(200, userLoginResponse.statusCode(), "User login failed");

        authToken = userLoginResponse.jsonPath().getString("data.token");

        Response response = SimpleActions.logout(authToken);

        assertAll("User is logged out",
                () -> assertEquals(USER_LOGOUT, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
