package api_tests.Users;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.Email;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.*;
import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;

public class ForgotPasswordTest extends BaseApiTest {
    @Test
    void resetPassword() {
        Email email = new Email(userEmail);

        Response response = SimpleActions.sendPasswordResetLink(email);

        assertAll("Reset password link is sent",
                () -> assertEquals(passwordResetLinkSent(userEmail), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    static Stream<Arguments> invalidEmailProvider() {
        return Stream.of(
                Arguments.of("Non existing email",
                        new Email(randomEmail()),
                        NO_ACCOUNT_WITH_EMAIL,
                        401),
                Arguments.of("Invalid Email",
                        new Email("invalid.mail.com"),
                        VALID_EMAIL_REQUIRED,
                        400),
                Arguments.of("Empty Email",
                        new Email("  "),
                        VALID_EMAIL_REQUIRED,
                        400),
                Arguments.of("Missing Email",
                        new Email(null),
                        VALID_EMAIL_REQUIRED,
                        400)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidEmailProvider")
    void updateUserProfileNegativeTests(String description,
                                        Email email,
                                        String expectedMessage,
                                        int expectedStatus) {

        Response response = SimpleActions.sendPasswordResetLink(email);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
