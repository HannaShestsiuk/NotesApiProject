package pages.practice_pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import pages.BasePage;

import static constants.Constants.FORGOT_PASSWORD_PAGE;

public class ForgotPasswordPage extends BasePage {

    public ForgotPasswordPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return FORGOT_PASSWORD_PAGE;
    }

    private Locator emailInput() {
        return page.locator("#email");
    }

    private Locator retrievePasswordButton() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Retrieve password")
        );
    }

    public Locator emailSentMessage = page.locator("#confirmation-alert p");

    public Locator invalidEmailMessage = page.locator(".invalid-feedback");

    private String normalize(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    public void waitForEmailSentMessage() {
        emailSentMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void waitForInvalidEmailMessage() {
        invalidEmailMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Fill Email")
    public void fillEmail(String email){
        emailInput().fill(email);
    }

    @Step("Click 'Retrieve password' button")
    public ForgotPasswordPage clickRetrievePassword(){
        retrievePasswordButton().click();
        page.waitForURL("**" + FORGOT_PASSWORD_PAGE);
        return new ForgotPasswordPage(page);
    }

    public ForgotPasswordPage fillForgotPasswordForm(String email){
        fillEmail(email);
        return clickRetrievePassword();
    }
}
