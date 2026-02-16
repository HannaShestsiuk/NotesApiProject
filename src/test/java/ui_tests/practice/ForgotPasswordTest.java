package ui_tests.practice;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.practice_pages.ForgotPasswordPage;
import pages.practice_pages.HomePage;
import ui_tests.BaseTest;

import java.util.stream.Stream;

import static constants.Messages.INVALID_EMAIL;
import static helpers.TestDataGenerator.randomEmail;

public class ForgotPasswordTest extends BaseTest {

    private static Stream<Arguments> negativeForgotPasswordDataProvider() {
        return Stream.of(
                Arguments.of("Empty email", "", INVALID_EMAIL),
                Arguments.of("Invalid Email", "testexample.com", INVALID_EMAIL)
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

        String email = randomEmail();

        HomePage home = new HomePage(page()).open();
        ForgotPasswordPage forgotPasswordPage = home.goToForgotPasswordPage();

        forgotPasswordPage.fillForgotPasswordForm(email);
        forgotPasswordPage.waitForEmailSentMessage();
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
    void forgotPasswordNegativeTest(String testName,
                                    String email,
                                    String expectedMessage) {
        HomePage home = new HomePage(page()).open();
        ForgotPasswordPage forgotPasswordPage = home.goToForgotPasswordPage();
        forgotPasswordPage.fillForgotPasswordForm(email);
        forgotPasswordPage.isAlertVisible(expectedMessage);
    }
}
