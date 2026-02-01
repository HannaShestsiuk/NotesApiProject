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

    public NotesAppRegistrationPage clickCreateAccountButton() {
        createAccountButton().click();
        return new NotesAppRegistrationPage(page);
    }

    public NotesAppLoginPage clickLoginButton() {
        loginButton().click();
        return new NotesAppLoginPage(page);
    }
}
