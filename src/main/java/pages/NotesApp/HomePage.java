package pages.NotesApp;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.BasePage;

public class HomePage extends BasePage {

    public HomePage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return "/";
    }

    private Locator createAccountButton() {
        return page.locator("[data-testid='open-register-view']");
    }

    private Locator loginButton() {
        return page.locator("[data-testid='open-login-view']");
    }

    public RegistrationPage clickCreateAccountButton() {
        createAccountButton().click();
        return new RegistrationPage(page);
    }

    public LoginPage clickLoginButton() {
        loginButton().click();
        return new LoginPage(page);
    }
}