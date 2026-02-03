package pages.PracticePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.BasePage;

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

    @Step("Logout user: {user.name()}")
    public SecurePage logout() {
        logoutButton().click();
        return this;
    }
}
