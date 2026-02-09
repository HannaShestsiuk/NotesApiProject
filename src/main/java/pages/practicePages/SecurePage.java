package pages.practicePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import pages.BasePage;

import static constants.Constants.LOGIN_PAGE;
import static constants.Constants.SECURE_PAGE;

public class SecurePage extends BasePage {
    public SecurePage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return "/secure";
    }

    public boolean isAt() {
        return page.url().endsWith("/secure");
    }

    public Locator flashMessage() {
        return page.locator("#flash-message b");
    }

    public String getFlashMessage() {
        return flashMessage().textContent().trim();
    }

    public Locator loginMessage() {
        return page.locator("b:has-text('You logged into a secure area')");
    }

    private Locator logoutButton() {
        return page.locator("button:has-text('Logout')");
    }

    @Step("Assert that Login Page is opened")
    public void securePageShouldBeOpened() {
        page.waitForURL("**" + SECURE_PAGE);

        page.getByRole(AriaRole.HEADING,
                        new Page.GetByRoleOptions().setName("Secure Area page"))
                .waitFor();
    }

    @Step("Logout user: {user.name()}")
    public SecurePage logout() {
        logoutButton().click();
        return this;
    }
}
