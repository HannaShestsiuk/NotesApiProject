package pages.practicePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import pages.BasePage;
import records.Email;

public class ForgotPasswordPage extends BasePage {

    public ForgotPasswordPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return "/forgot-password";
    }

    public boolean isAt() {
        return page.url().endsWith("/forgot-password");
    }

    private Locator emailField() {
        return page.locator("#email");
    }

    private Locator retrievePasswordButton() {
        return page.locator("button:has-text('Retrieve password')");
    }

    public Locator emailSentMessage() {
        return page.locator("#confirmation-alert p");
    }

    public Locator invalidEmailMessage() {
        return page.locator("div.invalid-feedback");
    }

    private String normalize(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    public String getEmailSentMessage() {
        return normalize(emailSentMessage().textContent());
    }

    public String getInvalidEmailMessage() {
        return invalidEmailMessage().textContent().trim();
    }

    public void waitForEmailSentMessage() {
        emailSentMessage().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void waitForInvalidEmailMessage() {
        invalidEmailMessage().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }


    @Step("Retrieve password")
    public ForgotPasswordPage retrievePassword(Email email) {
        emailField().fill(email.email());
        retrievePasswordButton().click();
        return this;
    }
}
