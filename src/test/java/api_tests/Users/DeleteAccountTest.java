package api_tests.Users;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import records.UserLogin;
import requests.SimpleActions;

import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteAccountTest extends BaseApiTest {

    @Test
    void deleteAccountTest() {

        Response response = SimpleActions.deleteAccount(authToken);

        assertAll("Account is deleted",
                () -> assertEquals(ACCOUNT_DELETED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );

        UserLogin userLogin = new UserLogin(userEmail, userPassword);

        Response userLoginResponse = SimpleActions.loginUser(userLogin);

        assertAll("Deleted user login failed",
                () -> assertEquals(LOGIN_INVALID_EMAIL_OR_PASSWORD, userLoginResponse.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, userLoginResponse.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(userLoginResponse.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
