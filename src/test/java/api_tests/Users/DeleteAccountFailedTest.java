package api_tests.Users;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import records.UserLogin;
import requests.SimpleActions;

import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteAccountFailedTest extends BaseApiTest {

    @Test
    void deleteAccountFailedTest() {

        Response response = SimpleActions.deleteAccount("");

        assertAll("Account is NOT deleted by non-registered user",
                () -> assertEquals(NO_AUTH_HEADER, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );

        UserLogin userLogin = new UserLogin(userEmail, userPassword);

        Response userLoginResponse = SimpleActions.loginUser(userLogin);

        assertAll("User is logged in",
                () -> assertEquals(LOGIN_SUCCESS, userLoginResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, userLoginResponse.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(userLoginResponse.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
