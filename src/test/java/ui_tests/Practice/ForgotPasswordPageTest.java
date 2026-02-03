package ui_tests.Practice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.PracticePages.ForgotPasswordPage;
import pages.PracticePages.HomePage;
import records.Email;
import ui_tests.BaseTest;

import java.util.stream.Stream;

import static constants.Messages.INVALID_EMAIL;
import static constants.Messages.PASSWORD_RESET_SENT;
import static org.junit.jupiter.api.Assertions.*;

public class ForgotPasswordPageTest extends BaseTest {

    private static Stream<Arguments> negativeForgotPasswordDataProvider() {
        return Stream.of(
                Arguments.of("Empty email", new Email(""), INVALID_EMAIL),
                Arguments.of("Invalid Email", new Email("testexample.com"), INVALID_EMAIL)
        );
    }


    @DisplayName("[UI]. Forgot Password page. Validate successful password retrieval")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Forgot Password Form' page.
    3. Enter a valid email address.
    4. Click on 'Retrieve password' button.
    5. Assert that a confirmation message is displayed.
    """)
    @Test
    void forgotPasswordPositiveTest() {

        Email email = new Email("practice@example.com");

        HomePage home = new HomePage(page()).open();
        ForgotPasswordPage forgotPasswordPage = home.forgotPasswordPageClick();

        forgotPasswordPage.retrievePassword(email);

        // Wait for success message
        forgotPasswordPage.emailSentMessage()
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        String actualMessage = forgotPasswordPage.getEmailSentMessage();
        System.out.println("Success message: " + actualMessage);

        assertAll(
                () -> assertTrue(forgotPasswordPage.isAt(), "User should remain on Forgot Password Page"),
                () -> assertEquals(PASSWORD_RESET_SENT, actualMessage)
        );
    }

    @DisplayName("[UI]. Forgot Password page. Validate negative email scenarios")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Forgot Password Form' page.
    3. Enter invalid email values.
    4. Click on 'Retrieve password' button.
    5. Assert that validation error message is displayed.
    """)
    @ParameterizedTest(name = "{0}")
    @MethodSource("negativeForgotPasswordDataProvider")
    void forgotPasswordNegativeTest(String testName, Email email, String expectedMessage) {

        HomePage home = new HomePage(page()).open();
        ForgotPasswordPage forgotPasswordPage = home.forgotPasswordPageClick();

        forgotPasswordPage.retrievePassword(email);

        // Wait for validation message
        forgotPasswordPage.invalidEmailMessage()
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        String actualMessage = forgotPasswordPage.getInvalidEmailMessage();
        System.out.println("Validation message: " + actualMessage);

        assertAll(
                () -> assertTrue(forgotPasswordPage.isAt(), "User should remain on Forgot Password Page"),
                () -> assertEquals(expectedMessage, actualMessage, "Correct validation message should be displayed")
        );
    }

}
