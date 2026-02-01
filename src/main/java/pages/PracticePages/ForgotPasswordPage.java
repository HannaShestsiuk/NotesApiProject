package pages.PracticePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
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

    private Locator emailField() {
        return page.locator("#email");
    }

    private Locator retrievePasswordButton() {
        return page.locator("button:has-text('Retrieve password')");
    }

    public Locator emailForPasswordResetSentMessage() {
        return page.locator("#confirmation-alert");
    }

    @Step("Retrieve password")
    public ForgotPasswordPage retrievePassword(Email email) {
        emailField().fill(String.valueOf(email));
        retrievePasswordButton().click();
        return this;
    }
}
