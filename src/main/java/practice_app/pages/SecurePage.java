package practice_app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import common.BasePage;

import static constants.Constants.LOGIN_PAGE;
import static constants.Constants.SECURE_PAGE;

public class SecurePage extends BasePage {
    public SecurePage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return SECURE_PAGE;
    }

    private Locator logoutButton() {
        return page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Logout")
        );
    }

    @Step("Click 'Logout' button")
    public LoginPage clickLogout(){
        logoutButton().click();
        page.waitForURL("**" + LOGIN_PAGE);
        return new LoginPage(page);
    }

    @Step("Logout user")
    public LoginPage logout() {
        return clickLogout();
    }

    @Step("Assert that Secure Page is opened")
    public void securePageShouldBeOpened() {
        page.waitForURL("**" + SECURE_PAGE);

        page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Secure Area page")
                ).waitFor();
    }
}
