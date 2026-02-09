package api_tests.users;

import api_tests.BaseApiTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.UserProfile;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.*;
import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateUserProfileTest extends BaseApiTest {

    private Stream<Arguments> validUserProfileProvider(){
        return Stream.of(
                Arguments.of(
                        "User Profile updated",
                        new UserProfile(
                            randomUserName(),
                            randomPhone(10),
                            randomCompany()
                        )
                ),
                Arguments.of(
                        "Name Length = MIN(4)",
                        new UserProfile(
                            randomUserName(4),
                            randomPhone(10),
                            randomCompany()
                        )
                ),
                Arguments.of(
                        "Name Length = MAX(30)",
                        new UserProfile(
                            randomUserName(30),
                            randomPhone(10),
                            randomCompany()
                        )
                ),
                Arguments.of(
                        "Phone Length = MIN(8)",
                        new UserProfile(
                            randomUserName(),
                            randomPhone(8),
                            randomCompany()
                        )
                ),
                Arguments.of(
                        "Phone Length = MAX(20)",
                        new UserProfile(
                            randomUserName(),
                            randomPhone(20),
                            randomCompany()
                        )
                ),
                Arguments.of(
                        "Blank phone",
                        new UserProfile(
                            randomUserName(),
                            " ",
                            randomCompany()
                        )
                ),
                Arguments.of(
                        "Company Length = MIN(4)",
                        new UserProfile(
                            randomUserName(),
                            randomPhone(11),
                            randomCompany(4)
                        )
                ),
                Arguments.of(
                        "Company Length = MAX(20)",
                        new UserProfile(
                            randomUserName(),
                            randomPhone(11),
                            randomCompany(20)
                        )
                ),
                Arguments.of(
                        "Blank Company",
                        new UserProfile(
                            randomUserName(),
                            randomPhone(11),
                            " ")
                )
        );
    }

    static Stream<Arguments> invalidUserProfileProvider() {
        return Stream.of(
                Arguments.of(
                        "No Auth",
                        new UserProfile(
                                randomUserName(),
                                randomPhone(10),
                                randomCompany()
                        ),
                        "",
                        NO_AUTH_HEADER,
                        401
                ),
                Arguments.of(
                        "Missing userName",
                        new UserProfile(
                                null,
                                randomPhone(10),
                                randomCompany()
                        ),
                        authToken,
                        VALID_USERNAME_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Empty userName",
                        new UserProfile(
                                "",
                                randomPhone(10),
                                randomCompany()
                        ),
                        authToken,
                        VALID_USERNAME_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Name Length < MIN(4)",
                        new UserProfile(
                                randomUserName(3),
                                randomPhone(10),
                                randomCompany()
                        ),
                        authToken,
                        VALID_USERNAME_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Title Length > MAX(30)",
                        new UserProfile(
                                randomUserName(31),
                                randomPhone(10),
                                randomCompany()
                        ),
                        authToken,
                        VALID_USERNAME_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Missing phone",
                        new UserProfile(
                                randomUserName(),
                                null,
                                randomCompany()
                        ),
                        authToken,
                        INVALID_REQUEST,
                        400
                ),
                Arguments.of(
                        "Phone Length < MIN(8)",
                        new UserProfile(
                                randomUserName(),
                                randomPhone(7),
                                randomCompany()
                        ),
                        authToken,
                        INVALID_PHONE,
                        400
                ),
                Arguments.of(
                        "Phone Length > MAX(20)",
                        new UserProfile(
                                randomUserName(),
                                randomPhone(21),
                                randomCompany()
                        ),
                        authToken,
                        INVALID_PHONE,
                        400
                ),
                Arguments.of(
                        "Missing company",
                        new UserProfile(
                                randomUserName(),
                                randomPhone(10),
                                null
                        ),
                        authToken,
                        INVALID_REQUEST,
                        400
                ),
                Arguments.of(
                        "Company Length < MIN(4)",
                        new UserProfile(
                                randomUserName(),
                                randomPhone(10),
                                randomCompany(3)
                        ),
                        authToken,
                        INVALID_COMPANY,
                        400
                ),
                Arguments.of(
                        "Company Length > MAX(30)",
                        new UserProfile(
                                randomUserName(),
                                randomPhone(10),
                                randomCompany(31)
                        ),
                        authToken,
                        INVALID_COMPANY,
                        400
                )
        );
    }

    @DisplayName("[API. User]. PATCH Method. Update user's profile")
    @Description("""
            1. Update user profile.
            2. Assert the response.
            3. Get updated user profile.
            4. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
    @MethodSource("validUserProfileProvider")
    void updateUserProfilePositiveTests(String description, UserProfile userProfile) {
        Response response = SimpleActions.updateUserProfile(userProfile, authToken);

        assertAll(description,
                () -> assertEquals(USER_PROFILE_UPDATED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status."),
                () -> assertEquals(userProfile.name(), response.jsonPath().getString("data.userName"), "Invalid userName."),
                () -> assertEquals(userProfile.phone(), response.jsonPath().getString("data.phone"), "Invalid phone."),
                () -> assertEquals(userProfile.company(), response.jsonPath().getString("data.company"), "Invalid company."),
                () -> assertEquals(userId, response.jsonPath().getString("data.id"), "Invalid user id.")
        );

        Response getProfileResponse = SimpleActions.getUserProfile(authToken);

        assertAll(description,
                () -> assertEquals(response.jsonPath().getString("data.userName"), getProfileResponse.jsonPath().getString("data.userName"), "Invalid userName."),
                () -> assertEquals(response.jsonPath().getString("data.phone"), getProfileResponse.jsonPath().getString("data.phone"),  "Invalid phone."),
                () -> assertEquals(response.jsonPath().getString("data.company"), getProfileResponse.jsonPath().getString("data.company"), "Invalid company.")
        );
    }

    @DisplayName("[API. User]. PATCH Method. Update user's profile")
    @Description("""
            1. Update user profile.
            2. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
    @MethodSource("invalidUserProfileProvider")
    void updateUserProfileNegativeTests(
            String description,
            UserProfile userProfile,
            String token,
            String expectedMessage,
            int expectedStatus
    ) {
        Response response = SimpleActions.updateUserProfile(userProfile, token);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
