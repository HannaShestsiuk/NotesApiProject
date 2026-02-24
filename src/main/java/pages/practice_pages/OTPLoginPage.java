package pages.practice_pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import pages.BasePage;
import records.practice_records.OneTimePasswordData;

import static constants.Constants.ONE_TIME_PASSWORD_PAGE;
import static constants.Constants.SECURE_PAGE;

/**
 * Page Object for the OTP Login practice page.
 * Provides methods to interact with the email submission form and handle
 * the transition to the OTP verification step.
 */

public class OTPLoginPage extends BasePage {

    /**
     * Constructs the OTP Login page instance.
     * @param page The Playwright Page instance.
     */
    public OTPLoginPage(Page page){
        super(page);
    }

    protected String path() {
        return ONE_TIME_PASSWORD_PAGE;
    }

    private final Locator emailInput = page.locator("#email");

    private Locator sendOTPButton() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Send OTP Code")
        );
    }

    private final Locator otpInput = page.locator("#otp");

    private Locator verifyOTPButton() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Verify OTP Code")
        );
    }

    /**
     * Submits the provided email address to request an OTP code.
     * This action usually triggers a transition to the second phase of login.
     * * @param email The email address to be entered into the input field.
     */
    @Step("Login with email: {email}")
    public OTPLoginPage requestOtp(String email) {
        emailInput.fill(email);
        sendOTPButton().click();
        return this;
    }

    /**
     * Fills the 6-digit OTP code into the verification field.
     * @param code The 6-digit OTP code.
     * @return This page object.
     */
    @Step("Fill OTP code: {code}")
    public OTPLoginPage fillOtpCode(String code) {
        otpInput.fill(code);
        return this;
    }

    /**
     * Clicks the 'Verify OTP Code' button and waits for the redirection to the Secure Page.
     * @return A new instance of SecurePage.
     */
    @Step("Click 'Verify OTP Code' button")
    public SecurePage clickVerifyOTPButton(){
        verifyOTPButton().click();
        page.waitForURL("**" + SECURE_PAGE);
        return new SecurePage(page);
    }

    /**
     * Performs the full OTP login sequence: entering email, requesting code,
     * entering code, and clicking verify.
     * @param data The OneTimePasswordData record.
     * @return A new instance of SecurePage.
     */
    @Step("Login with OTP using data: {data}")
    public SecurePage loginWithOtp(OneTimePasswordData data) {
        return requestOtp(data.email())
                .fillOtpCode(data.otpCode())
                .clickVerifyOTPButton();
    }
}
