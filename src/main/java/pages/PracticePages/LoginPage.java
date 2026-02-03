package pages.PracticePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.BasePage;
import records.PracticeRecords.PracticeUiUser;

public class LoginPage extends BasePage {
    public LoginPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return "/login";
    }

    public boolean isAt() {
        return page.url().endsWith("/login");
    }

    private Locator usernameField() {
        return page.locator("#username");
    }

    private Locator passwordField() {
        return page.locator("#password");
    }

    private Locator loginButton() {
        return page.locator("button:has-text('Login')");
    }

    public Locator flashMessage() {
        return page.locator("#flash-message b");
    }

    public String getFlashMessage() {
        return flashMessage().textContent().trim();
    }

    public Locator successMessage() {
        return page.locator("b:has-text('Successfully registered, you can log in now.')");
    }

    public String loginPageTitle() {
        return page.locator("h1").textContent().trim();
    }

    public Locator usernameInvalidMessage() {
        return page.locator("b:has-text('Your username is invalid')");
    }

    public Locator passwordInvalidMessage() {
        return page.locator("b:has-text('Your password is invalid')");
    }

    @Step("Login user: {user}")
    public LoginPage login(PracticeUiUser user) {
        usernameField().fill(user.name());
        passwordField().fill(user.password());
        loginButton().click();
        return this;
    }
}
