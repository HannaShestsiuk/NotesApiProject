package api_tests.users;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.Password;
import records.UserLogin;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.*;
import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangePasswordTest extends BaseApiTest {

    static Stream<Arguments> invalidChangePasswordProvider() {
        return Stream.of(
                Arguments.of(
                        "No Auth",
                        new Password(
                                userPassword,
                                randomPassword(8, 10)
                        ),
                        "",
                        NO_AUTH_HEADER,
                        401
                ),
                Arguments.of(
                        "Missing current password",
                        new Password(
                                null,
                                randomPassword(8, 10)
                        ),
                        authToken,
                        VALID_CURRENT_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Empty current password",
                        new Password(
                                " ",
                                randomPassword(8, 10)
                        ),
                        authToken,
                        VALID_CURRENT_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Current Password Length < MIN(6)",
                        new Password(
                                randomPassword(6,7).substring(0,5),
                                randomPassword(8, 10)
                        ),
                        authToken,
                        VALID_CURRENT_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Current Password Length > MAX(30)",
                        new Password(
                                randomPassword(32, 33).substring(0,31),
                                randomPassword(8, 10)
                        ),
                        authToken,
                        VALID_CURRENT_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Current Password does NOT match",
                        new Password(
                                randomPassword(8, 10),
                                randomPassword(8, 10)
                        ),
                        authToken,
                        CURRENT_PASSWORD_INCORRECT,
                        400
                ),
                Arguments.of(
                        "Missing new password",
                        new Password(
                                userPassword,
                                null
                        ),
                        authToken,
                        VALID_NEW_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Empty new password",
                        new Password(
                                userPassword,
                                " "
                        ),
                        authToken,
                        VALID_NEW_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "New Password Length < MIN(6)",
                        new Password(
                                userPassword,
                                randomPassword(6,7).substring(0,5)
                        ),
                        authToken,
                        VALID_NEW_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "New Password Length > MAX(30)",
                        new Password(
                                userPassword,
                                randomPassword(32,33).substring(0,31)
                        ),
                        authToken,
                        VALID_NEW_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "New Password matches Current Password)",
                        new Password(
                                userPassword,
                                userPassword
                        ),
                        authToken,
                        NEW_AND_CURRENT_PASSWORDS_EQUAL,
                        400
                )
        );
    }

    @DisplayName("[API. User]. POST Method. Change Password")
    @Description("""
            1. Update password.
            2. Assert the response.
            3. Logout user.
            4. Login user with the new password.
            5. Assert the response.
            """)
    @Test
    void changePassword() {
        Password password = new Password(
                userPassword,
                randomPassword(6,30)
        );

        Response response = SimpleActions.changePassword(password, authToken);

        assertAll("Password is changed",
                () -> assertEquals(PASSWORD_CHANGED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );

        userPassword = password.newPassword();

        Response logoutResponse = SimpleActions.logout(authToken);

        assertEquals(200, logoutResponse.statusCode(), "User login failed");

        UserLogin userLogin = new UserLogin(userEmail, userPassword);

        Response userLoginResponse = SimpleActions.loginUser(userLogin);

        assertEquals(200, userLoginResponse.statusCode(), "User login failed");
    }

    @DisplayName("[API. User]. POST Method. Change Password")
    @Description("""
            1. Update password.
            2. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
    @MethodSource("invalidChangePasswordProvider")
    void changePasswordNegativeTests(
            String description,
            Password password,
            String token,
            String expectedMessage,
            int expectedStatus
    ) {
        Response response = SimpleActions.changePassword(password, token);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
