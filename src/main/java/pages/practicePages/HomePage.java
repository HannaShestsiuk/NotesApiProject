package pages.practicePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import pages.BasePage;

import static constants.Constants.*;

public class HomePage extends BasePage {

    private final Locator registerPageLink = page.getByRole(
            AriaRole.LINK,
            new Page.GetByRoleOptions()
                    .setName("Test Register Page")
    );

    private final Locator loginPageLink = page.getByRole(
            AriaRole.LINK,
            new Page.GetByRoleOptions()
                    .setName("Test Login Page")
    );

    private final Locator forgotPasswordPageLink = page.getByRole(
            AriaRole.LINK,
            new Page.GetByRoleOptions()
                    .setName("Forgot Password Form")
    );

    public HomePage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return "/";
    }

    private void safeClickAndWait(Locator element, String urlPattern, String headerText){
        element.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));

        element.click(new Locator.ClickOptions().setNoWaitAfter(true));

        page.waitForLoadState();

        page.waitForURL(urlPattern,
                new Page.WaitForURLOptions().setTimeout(5000));

        page.waitForSelector("//h1[contains(.,'" + headerText + "')]",
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(5000));
    }

    protected void shouldBeVisible(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));
    }

    protected Locator visible(Locator locator) {
        shouldBeVisible(locator);
        return locator;
    }


    @Step("Open Register Page")
    public RegisterPage goToRegisterPage() {
        safeClickAndWait(registerPageLink,
                "**" + REGISTER_PAGE,
                "Test Register page");
        return new RegisterPage(page);
    }

    @Step("Open Login Page")
    public LoginPage goToLoginPage() {
        safeClickAndWait(loginPageLink,
                "**" + LOGIN_PAGE,
                "Test Login page");
        return new LoginPage(page);
    }

    @Step("Open Forgot Password Form")
    public ForgotPasswordPage goToForgotPasswordPage() {
        safeClickAndWait(forgotPasswordPageLink,
                "**" + FORGOT_PASSWORD_PAGE,
                "Forgot Password form");
        return new ForgotPasswordPage(page);
    }
}
