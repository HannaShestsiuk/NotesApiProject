package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import records.UiUser;

public class LoginPage extends BasePage {

    public LoginPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return "/login"; // or whatever your app uses
    }

    private Locator emailAddressField() {
        return page.locator("[data-testid='login-email']");
    }

    private Locator passwordField() {
        return page.locator("[data-testid='login-password']");
    }

    private Locator loginButton() {
        return page.locator("[data-testid='login-submit']");
    }

    public LoginPage fillForm(UiUser user) {
        emailAddressField().fill(user.email());
        passwordField().fill(user.password());
        return this;
    }

    public void login() {
        loginButton().click();
    }
}

