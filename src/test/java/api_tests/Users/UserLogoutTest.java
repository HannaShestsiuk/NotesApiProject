package api_tests.Users;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import requests.SimpleActions;

import static constants.Messages.USER_LOGOUT;
import static org.junit.jupiter.api.Assertions.*;

public class UserLogoutTest extends BaseApiTest {
    @Test
    void logoutUserTest() {

        Response response = SimpleActions.logout(authToken);

        assertAll("User is logged out",
                () -> assertEquals(USER_LOGOUT, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
