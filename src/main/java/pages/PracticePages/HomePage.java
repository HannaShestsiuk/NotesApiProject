package pages.PracticePages;

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

    private Locator registerPageButton() {
        return page.locator("a[href='/register']");
    }

    private Locator loginPageButton() {
        return page.locator("a[href='/login']");
    }

    private Locator forgotPasswordPageButton() {
        return page.locator("a[href='/forgot-password']");
    }

    public RegisterPage registerPageClick() {
        registerPageButton().click();
        return new RegisterPage(page);
    }

    public LoginPage loginPageClick() {
        loginPageButton().click();
        return new LoginPage(page);
    }

    public ForgotPasswordPage forgotPasswordPageClick() {
        forgotPasswordPageButton().click();
        return new ForgotPasswordPage(page);
    }
}
