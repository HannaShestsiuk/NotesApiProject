package pages.PracticePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.BasePage;
import records.UiUser;

public class LoginPage extends BasePage {
    public LoginPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return "/login";
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

    public Locator loginMessage() {
        return page.locator("b:has-text('You logged into a secure area')");
    }

    public Locator usernameInvalidMessage() {
        return page.locator("b:has-text('Your username is invalid')");
    }

    public Locator passwordInvalidMessage() {
        return page.locator("b:has-text('Your password is invalid')");
    }

    @Step("Login user: {user.name()}")
    public LoginPage login(UiUser user) {
        usernameField().fill(user.name());
        passwordField().fill(user.password());
        loginButton().click();
        return this;
    }
}
