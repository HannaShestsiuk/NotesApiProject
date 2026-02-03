package ui_tests.Practice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.PracticePages.HomePage;
import pages.PracticePages.LoginPage;
import pages.PracticePages.SecurePage;
import records.PracticeRecords.PracticeUiUser;
import ui_tests.BaseTest;

import java.util.stream.Stream;

import static constants.Messages.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPageTest extends BaseTest {

    private static Stream<Arguments> negativeLoginTestDataProvider() {

        String validUsername = globalUser.getName() ;
        String validPassword = globalUser.getPassword();

        return Stream.of(
                Arguments.of(
                        "Invalid username",
                        new PracticeUiUser("wrongUser", validPassword, validPassword),
                        LOGIN_INVALID_PASSWORD
                ),
                Arguments.of(
                        "Invalid password",
                        new PracticeUiUser(validUsername, "WrongPassword!", "WrongPassword!"),
                        LOGIN_INVALID_PASSWORD
                ),
                Arguments.of(
                        "Empty username",
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
    3. Enter valid username and password.
    4. Click on Login button.
    5. Assert that user is redirected to 'Secure Area' page.
    6. Assert that success alert message is displayed.
    """)
    @Test
    void userLoginPageTest() {

        HomePage home = new HomePage(page()).open();
        LoginPage loginPage = home.loginPageClick();

        loginPage.login(globalUser);

        SecurePage securePage = new SecurePage(page());

        // Assert user is on Secure Page
        assertTrue(securePage.isAt(), "User should be on Secure Page");

        // Assert success message
        String actualMessage = securePage.getFlashMessage();
        System.out.println("Login success message: " + actualMessage);

        assertEquals(SUCCESSFUL_LOGIN, actualMessage, "Success login message is displayed");
    }

    @DisplayName("[UI]. Login page. Validate negative login scenarios")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Test Login Page'.
    3. Enter invalid username or password.
    4. Click on Login button.
    5. Assert that user stays on 'Test Login Page'.
    6. Assert that correct error alert message is displayed.
    """)
    @ParameterizedTest(name = "{0}")
    @MethodSource("negativeLoginTestDataProvider")
    void negativeLoginTest(String testName, PracticeUiUser user, String expectedMessage) {

        HomePage home = new HomePage(page()).open();
        LoginPage loginPage = home.loginPageClick();

        loginPage.login(user);

        loginPage.flashMessage().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );

        String actualMessage = loginPage.flashMessage().textContent().trim();
        System.out.println("Actual error: " + actualMessage);

        assertAll(
                () -> assertTrue(loginPage.isAt(), "User should remain on Login Page"),
                () -> assertEquals(expectedMessage, actualMessage, "Correct error message should be displayed")
        );
    }
}
