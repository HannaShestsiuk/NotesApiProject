package ui_tests.practice;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice_app.pages.HomePage;
import practice_app.pages.LoginPage;
import practice_app.pages.OTPLoginPage;
import practice_app.pages.SecurePage;
import records.practice_records.OneTimePasswordData;
import ui_tests.BaseTest;

import static constants.Messages.LOGOUT_MESSAGE;
import static constants.Messages.SUCCESSFUL_LOGIN;

public class OTPLoginTest extends BaseTest {
    @Test
    @DisplayName("[UI]. OTP page. Verify successful login with valid OTP")
    @Description("""
        1. Open the Home Page (https://practice.expandtesting.com/).
        2. Navigate to the 'OTP Login' page via the Home Page menu.
        3. Enter a valid email address and click 'Send OTP Code'.
        4. Enter the 6-digit OTP code and click 'Verify OTP Code'.
        5. Assert that the user is redirected to the 'Secure Area' page.
        6. Assert that the successful login alert message is displayed.
        7. Click the 'Logout' button.
        8. Assert that the user is redirected back to the 'Login Page'.
        9. Assert that the successful logout alert message is displayed.
        """)
    void loginWithOneTimePasswordTest() {

        OneTimePasswordData validData = OneTimePasswordData.defaultOtp();

        HomePage home = new HomePage(page()).open();

        OTPLoginPage otpPage = home.goToOtpLoginPage();

        SecurePage securePage = otpPage.loginWithOtp(validData);
        securePage.securePageShouldBeOpened();
        securePage.isAlertVisible(SUCCESSFUL_LOGIN);

        LoginPage loginPageAfterLogout = securePage.logout();
        loginPageAfterLogout.loginPageShouldBeOpened();
        loginPageAfterLogout.isAlertVisible(LOGOUT_MESSAGE);
    }
}
