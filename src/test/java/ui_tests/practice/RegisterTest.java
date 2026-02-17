package ui_tests.practice;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.practice_pages.HomePage;
import pages.practice_pages.LoginPage;
import pages.practice_pages.RegisterPage;
import pages.practice_pages.SecurePage;
import records.practice_records.PracticeUiUser;
import ui_tests.BaseTest;

import java.util.stream.Stream;

import static constants.Messages.*;
import static helpers.TestDataGenerator.*;

public class RegisterTest extends BaseTest {

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
                        "Existing userName",
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
    void userRegistrationTest() {

        String password = randomPassword(8, 10);
        String userName = randomName();
        PracticeUiUser user = new PracticeUiUser(userName, password, password);

        HomePage home = new HomePage(page()).open();
        RegisterPage registerPage = home.goToRegisterPage();

        LoginPage loginPage = registerPage.fillRegisterForm(user);
        loginPage.loginPageShouldBeOpened();
        loginPage.isAlertVisible(SUCCESSFUL_REGISTRATION);

        SecurePage securePage = loginPage.fillLoginForm(user);
        securePage.securePageShouldBeOpened();
        securePage.isAlertVisible(SUCCESSFUL_LOGIN);

        LoginPage loginPageAfterLogout = securePage.logout();
        loginPageAfterLogout.loginPageShouldBeOpened();
        loginPageAfterLogout.isAlertVisible(LOGOUT_MESSAGE);
    }

    @DisplayName("[UI]. Registration page. Validate negative registration scenarios")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Test Register Page' page.
    3. Fill in the registration form with invalid data:
       - Empty userName
       - Empty password
       - Empty confirm password
       - Password and confirm password do not match
       - Existing userName ('practice')
    4. Click on Register button.
    5. Assert that the displayed error message matches the expected one.
    """)
    @ParameterizedTest(name = "{0}")
    @MethodSource("negativeRegistrationTestDataProvider")
    void negativeUserRegistrationTest(String testName,
                                  PracticeUiUser user,
                                  String expectedMessage) {
        HomePage home = new HomePage(page()).open();
        RegisterPage registerPage = home.goToRegisterPage();
        registerPage.fillRegisterFormExpectingFailure(user);
        registerPage.isAlertVisible(expectedMessage);
    }
}
