package ui_tests.Practice;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.PracticePages.HomePage;
import pages.PracticePages.LoginPage;
import pages.PracticePages.RegisterPage;
import pages.PracticePages.SecurePage;
import records.PracticeRecords.PracticeUiUser;
import ui_tests.BaseTest;

import java.util.stream.Stream;

import static constants.Messages.*;
import static helpers.TestDataGenerator.*;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterPageTest extends BaseTest {

    private static Stream<Arguments> negativeRegistrationTestDataProvider() {

        String validUsername = randomName();
        String validPassword = randomPassword(8, 10);

        return Stream.of(
                Arguments.of(
                        "Empty username",
                        new PracticeUiUser("", validPassword, validPassword),
                        FIELDS_REQUIRED
                ),
                Arguments.of(
                        "Empty password",
                        new PracticeUiUser(validUsername, "", ""),
                        FIELDS_REQUIRED
                ),
                Arguments.of(
                        "Empty confirmPassword",
                        new PracticeUiUser(validUsername, validPassword, ""),
                        FIELDS_REQUIRED
                ),
                Arguments.of(
                        "Passwords do not match",
                        new PracticeUiUser(validUsername, validPassword, validPassword + "x"),
                        PASSWORDS_NOT_MATCH
                ),
                Arguments.of(
                        "Existing username",
                        new PracticeUiUser("practice", validPassword, validPassword),
                        USERNAME_IS_TAKEN
                )
        );
    }


    @DisplayName("[UI]. Registration page. Validate successful user registration")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Test Register Page' page.
    3. Fill in registration form.
    4. Click on Register button.
    5. Assert that user is redirected to 'Test Login Page'.
    6. Assert that alert message is displayed.
    7. Login as the registered user.
    8. Assert that registered user is logged-in.
    """)
    @Test
    void userRegistrationPageTest() {

        String password = randomPassword(8, 10);
        String username = randomName();
        PracticeUiUser user = new PracticeUiUser(username, password, password);

        System.out.println(user);

        HomePage home = new HomePage(page()).open();
        RegisterPage registerPage = home.registerPageClick();

        registerPage.fillForm(user)
                .register();

        // If registration failed → stop test
        if (registerPage.flashMessage().isVisible()) {
            String error = registerPage.getFlashMessage();
            System.out.println("Registration error: " + error);
            fail("Registration failed");
        }

        LoginPage loginPage = new LoginPage(page());

        String success = loginPage.getFlashMessage();
        System.out.println("Registration success: " + success);

        assertAll(
                () -> assertTrue(loginPage.isAt(), "User should be on Login Page"),
                () -> assertEquals(SUCCESSFUL_REGISTRATION, success, "Success registration message is displayed")
        );

        if (!loginPage.isAt() && !loginPage.successMessage().isVisible()) {
            fail("Registration failed, login step is skipped");
        }

        loginPage.login(user);

        SecurePage securePage = new SecurePage(page());
        String successLogin = securePage.getFlashMessage();
        assertEquals(SUCCESSFUL_LOGIN, successLogin, "Success login message is displayed");
    }

    @DisplayName("[UI]. Registration page. Validate negative registration scenarios")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Test Register Page' page.
    3. Fill in the registration form with invalid data:
       - Empty username
       - Empty password
       - Empty confirm password
       - Password and confirm password do not match
       - Existing username ('practice')
    4. Click on Register button.
    5. Assert that the displayed error message matches the expected one.
    """)
    @ParameterizedTest(name = "{0}")
    @MethodSource("negativeRegistrationTestDataProvider")
    void negativeRegistrationTest(String testName, PracticeUiUser user, String expectedMessage) {

        HomePage home = new HomePage(page()).open();
        RegisterPage registerPage = home.registerPageClick();

        registerPage.fillForm(user).register();

        String actualMessage = registerPage.getFlashMessage();
        System.out.println("Actual error: " + actualMessage);

        assertEquals(expectedMessage, actualMessage, "Correct error message should be displayed");
    }
}
