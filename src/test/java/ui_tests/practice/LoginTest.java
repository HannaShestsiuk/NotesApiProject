package ui_tests.practice;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.practice_pages.HomePage;
import pages.practice_pages.LoginPage;
import pages.practice_pages.SecurePage;
import records.practice_records.PracticeUiUser;
import ui_tests.BaseTest;

import java.util.stream.Stream;

import static constants.Messages.*;

public class LoginTest extends BaseTest {

    private static Stream<Arguments> negativeLoginTestDataProvider() {

        String validUsername = globalUser.getUserName() ;
        String validPassword = globalUser.getPassword();

        return Stream.of(
                Arguments.of(
                        "Invalid userName",
                        new PracticeUiUser("wrongUser", validPassword, validPassword),
                        LOGIN_INVALID_PASSWORD
                ),
                Arguments.of(
                        "Invalid password",
                        new PracticeUiUser(validUsername, "WrongPassword!", "WrongPassword!"),
                        LOGIN_INVALID_PASSWORD
                ),
                Arguments.of(
                        "Empty userName",
                        new PracticeUiUser("", validPassword, validPassword),
                        LOGIN_INVALID_USERNAME
                ),
                Arguments.of(
                        "Empty password",
                        new PracticeUiUser(validUsername, "", ""),
                        LOGIN_INVALID_PASSWORD
                )
        );
    }


    @DisplayName("[UI]. Login page. Validate successful user login")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Test Login Page'.
    3. Enter valid userName and password.
    4. Click on Login button.
    5. Assert that user is redirected to 'Secure Area' page.
    6. Assert that success alert message is displayed.
    """)
    @Test
    void userLoginPageTest() {

        HomePage home = new HomePage(page()).open();
        LoginPage loginPage = home.goToLoginPage();

        loginPage.fillLoginForm(globalUser);

        SecurePage securePage = new SecurePage(page());

        securePage.securePageShouldBeOpened();
        securePage.flashMessage().shouldBeVisible();

    }

    @DisplayName("[UI]. Login page. Validate negative login scenarios")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Test Login Page'.
    3. Enter invalid userName or password.
    4. Click on Login button.
    5. Assert that user stays on 'Test Login Page'.
    6. Assert that correct error alert message is displayed.
    """)
    @ParameterizedTest(name = "{0}")
    @MethodSource("negativeLoginTestDataProvider")
    void negativeLoginTest(String testName,
                           PracticeUiUser user,
                           String expectedMessage) {
        HomePage home = new HomePage(page()).open();
        LoginPage loginPage = home.goToLoginPage();
        loginPage.fillLoginFormExpectingFailure(user);
        loginPage.isAlertVisible(expectedMessage);
    }
}
