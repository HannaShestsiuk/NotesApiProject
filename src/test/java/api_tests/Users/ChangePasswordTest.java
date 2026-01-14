package api_tests.Users;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.Password;
import records.User;
import records.UserLogin;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.*;
import static constants.ApiConstants.BASE_SCHEMA;
import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.assertResponseSchema;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangePasswordTest extends BaseApiTest {

    private static String localToken;
    private static String localEmail;
    private static String localPassword;

    // Create isolated local user for positive tests
    @BeforeAll
    static void setupLocalUser() {
        String name = randomUserName();
        localEmail = randomEmail();
        localPassword = randomPassword(6,7);

        User user = new User(name, localEmail, localPassword);

        Response registerUserResponse = SimpleActions.registerUser(user);
        assertEquals(201, registerUserResponse.statusCode(), "Local User registration failed");

        Response loginUserResponse = SimpleActions.loginUser(new UserLogin(localEmail, localPassword));
        assertEquals(200, loginUserResponse.statusCode(), "Local User login failed");

        localToken = loginUserResponse.jsonPath().getString("data.token");
    }

    private static Stream<Arguments> invalidChangePasswordProvider() {
        return Stream.of(
                Arguments.of(
                        "No Auth",
                        new Password(
                                "USE_CURRENT_PASSWORD",
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
                        "USE_VALID_TOKEN",
                        VALID_CURRENT_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Empty current password",
                        new Password(
                                " ",
                                randomPassword(8, 10)
                        ),
                        "USE_VALID_TOKEN",
                        VALID_CURRENT_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Current Password Length < MIN(6)",
                        new Password(
                                randomPassword(6,7).substring(0,5),
                                randomPassword(8, 10)
                        ),
                        "USE_VALID_TOKEN",
                        VALID_CURRENT_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Current Password Length > MAX(30)",
                        new Password(
                                randomPassword(32, 33).substring(0,31),
                                randomPassword(8, 10)
                        ),
                        "USE_VALID_TOKEN",
                        VALID_CURRENT_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Current Password does NOT match",
                        new Password(
                                randomPassword(8, 10),
                                randomPassword(8, 10)
                        ),
                        "USE_VALID_TOKEN",
                        CURRENT_PASSWORD_INCORRECT,
                        400
                ),
                Arguments.of(
                        "Missing new password",
                        new Password(
                                "USE_CURRENT_PASSWORD",
                                null
                        ),
                        "USE_VALID_TOKEN",
                        VALID_NEW_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Empty new password",
                        new Password(
                                "USE_CURRENT_PASSWORD",
                                " "
                        ),
                        "USE_VALID_TOKEN",
                        VALID_NEW_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "New Password Length < MIN(6)",
                        new Password(
                                "USE_CURRENT_PASSWORD",
                                randomPassword(6,7).substring(0,5)
                        ),
                        "USE_VALID_TOKEN",
                        VALID_NEW_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "New Password Length > MAX(30)",
                        new Password(
                                "USE_CURRENT_PASSWORD",
                                randomPassword(32,33).substring(0,31)
                        ),
                        "USE_VALID_TOKEN",
                        VALID_NEW_PASSWORD_REQUIRED,
                        400
                ),
                Arguments.of(
                        "New Password matches Current Password",
                        new Password(
                                "USE_CURRENT_PASSWORD",
                                "USE_CURRENT_PASSWORD"
                        ),
                        "USE_VALID_TOKEN",
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
    void changeUserPasswordTest() {
        Password password = new Password(
                localPassword,
                randomPassword(6,30)
        );

        Response response = SimpleActions.changePassword(password, localToken);

        assertResponseSchema(BASE_SCHEMA, response);

        assertAll("Password is changed",
                () -> assertEquals(PASSWORD_CHANGED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );

        localPassword = password.newPassword();

        Response logoutResponse = SimpleActions.logout(localToken);
        assertEquals(200, logoutResponse.statusCode(), "User login failed");

        UserLogin userNewLogin = new UserLogin(localEmail, localPassword);

        Response userNewLoginResponse = SimpleActions.loginUser(userNewLogin);
        assertEquals(200, userNewLoginResponse.statusCode(), "User login failed");
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
        //Create isolated local user
        String name = randomUserName();
        String email = randomEmail();
        String currentPassword = randomPassword(6,7);

        User user = new User(name, email, currentPassword);

        Response registerUser = SimpleActions.registerUser(user);
        assertEquals(201, registerUser.statusCode(), "User registration failed");

        UserLogin userLogin = new UserLogin(email, currentPassword);
        Response loginUser = SimpleActions.loginUser(userLogin);
        assertEquals(200, loginUser.statusCode(), "User login failed");

        String authToken = loginUser.jsonPath().getString("data.token");

        // Replace placeholders
        String tokenToUse = token.equals("USE_VALID_TOKEN") ? authToken : token;

        String currentPasswordToUse =
                password.currentPassword() != null &&
                        password.currentPassword().equals("USE_CURRENT_PASSWORD")
                        ? currentPassword : password.currentPassword();

        String newPasswordToUse =
                password.newPassword() != null &&
                        password.newPassword().equals("USE_CURRENT_PASSWORD")
                        ? currentPassword : password.newPassword();

        Password finalPassword = new Password(
                currentPasswordToUse,
                newPasswordToUse
        );

        Response response = SimpleActions.changePassword(finalPassword, tokenToUse);

        assertResponseSchema(BASE_SCHEMA, response);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
