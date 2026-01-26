package api_tests.Users;

import api_tests.BaseApiTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.User;
import records.UserLogin;
import records.UserProfile;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.*;
import static constants.ApiConstants.*;
import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.assertResponseSchema;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateUserProfileTest extends BaseApiTest {

    private static Stream<Arguments> validUserProfileProvider(){
        return Stream.of(
                Arguments.of(
                        "all valid user data",
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

    private static Stream<Arguments> invalidUserProfileProvider() {
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
                        "Missing name",
                        new UserProfile(
                                null,
                                randomPhone(10),
                                randomCompany()
                        ),
                        "USE_VALID_TOKEN",
                        VALID_USERNAME_REQUIRED,
                        400
                ),
                Arguments.of(
                        "Empty name",
                        new UserProfile(
                                "",
                                randomPhone(10),
                                randomCompany()
                        ),
                        "USE_VALID_TOKEN",
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
                        "USE_VALID_TOKEN",
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
                        "USE_VALID_TOKEN",
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
                        "USE_VALID_TOKEN",
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
                        "USE_VALID_TOKEN",
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
                        "USE_VALID_TOKEN",
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
                        "USE_VALID_TOKEN",
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
                        "USE_VALID_TOKEN",
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
                        "USE_VALID_TOKEN",
                        INVALID_COMPANY,
                        400
                )
        );
    }

    @DisplayName("[API. User]. PATCH Method. Update user's profile")
    @Description("""
            1. Register user
            2. Login user.
            3. Update user profile.
            4. Assert the response.
            5. Get updated user profile.
            6. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
    @MethodSource("validUserProfileProvider")
    void updateUserProfilePositiveTests(String description, UserProfile userProfile) {

        // Create isolated local user
        String name = randomUserName();
        String email = randomEmail();
        String password = randomPassword(8, 12);

        User user = new User(name, email, password);
        Response register = SimpleActions.registerUser(user);
        assertEquals(201, register.statusCode(), "User registration failed.");

        UserLogin login = new UserLogin(email, password);
        Response loginResponse = SimpleActions.loginUser(login);
        assertEquals(200, loginResponse.statusCode(), "User login failed.");

        String token = loginResponse.jsonPath().getString("data.token");
        String userId = loginResponse.jsonPath().getString("data.id");

        Response updateUserProfile = SimpleActions.updateUserProfile(userProfile, token);

        assertResponseSchema(USER_UPDATE_PROFILE_SCHEMA, updateUserProfile);

        assertAll(description,
                () -> assertEquals(USER_PROFILE_UPDATED, updateUserProfile.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, updateUserProfile.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(updateUserProfile.jsonPath().getBoolean("success"), "Invalid success status."),
                () -> assertEquals(userProfile.name(), updateUserProfile.jsonPath().getString("data.name"), "Invalid name."),
                () -> assertEquals(userProfile.phone(), updateUserProfile.jsonPath().getString("data.phone"), "Invalid phone."),
                () -> assertEquals(userProfile.company(), updateUserProfile.jsonPath().getString("data.company"), "Invalid company."),
                () -> assertEquals(userId, updateUserProfile.jsonPath().getString("data.id"), "Invalid user id.")
        );

        // Verify updated user profile via GET
        Response getProfileResponse = SimpleActions.getUserProfile(token);

        assertAll(description,
                () -> assertEquals(updateUserProfile.jsonPath().getString("data.name"), getProfileResponse.jsonPath().getString("data.name"), "Invalid name."),
                () -> assertEquals(updateUserProfile.jsonPath().getString("data.phone"), getProfileResponse.jsonPath().getString("data.phone"),  "Invalid phone."),
                () -> assertEquals(updateUserProfile.jsonPath().getString("data.company"), getProfileResponse.jsonPath().getString("data.company"), "Invalid company.")
        );

        registerLoggedInUser(token);
    }

    @DisplayName("[API. User]. PATCH Method. Update user's profile")
    @Description("""
            1. Register user.
            2. Login user.
            3. Update user profile.
            4. Assert the response.
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

        // Create isolated local user
        String name = randomUserName();
        String email = randomEmail();
        String password = randomPassword(8, 12);

        User user = new User(name, email, password);
        Response register = SimpleActions.registerUser(user);
        assertEquals(201, register.statusCode(), "User registration failed.");

        UserLogin login = new UserLogin(email, password);
        Response loginResponse = SimpleActions.loginUser(login);
        assertEquals(200, loginResponse.statusCode(), "User login failed.");

        String authToken = loginResponse.jsonPath().getString("data.token");

        // Replace placeholder
        String tokenToUse = token.equals("USE_VALID_TOKEN") ? authToken : token;

        Response response = SimpleActions.updateUserProfile(userProfile, tokenToUse);

        assertResponseSchema(BASE_SCHEMA, response);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );

        registerLoggedInUser(tokenToUse);
    }
}
