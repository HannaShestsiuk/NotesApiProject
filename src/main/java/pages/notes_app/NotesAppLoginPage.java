package pages.notes_app;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.BasePage;
import records.User;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Constants.*;

/**
 * Page Object for the Notes App Login page.
 * Provides functionality for user authentication and navigation to
 * password recovery or registration.
 */
public class NotesAppLoginPage extends BasePage {

    /**
     * Initializes the Notes App Login page.
     * @param page The Playwright Page instance.
     */
    public NotesAppLoginPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return NOTES_LOGIN_PAGE;
    }

    // --- Locators ---

    private Locator loginHeading() {
        return page.locator("h1");
    }

    private Locator emailInput() {
        return page.getByTestId("login-email");
    }

    private Locator passwordInput() {
        return page.getByTestId("login-password");
    }

    private Locator loginButton() {
        return page.getByTestId("login-submit");
    }

    private Locator forgotPasswordLink() {
        return page.locator("#forgotPasswordLink");
    }

    private Locator alertMessage() {
        return page.getByTestId("alert-message");
    }

    // --- Assertions & Validations ---

    /**
     * Verifies that the Login page is correctly loaded.
     * @return This page instance.
     */
    @Step("Verify that Notes App Login page is loaded")
    public NotesAppLoginPage loginPageShouldBeOpened() {
        assertThat(page).hasURL(Pattern.compile(".*" + NOTES_LOGIN_PAGE));
        assertThat(loginHeading()).hasText("Login");
        assertThat(loginButton()).isVisible();
        return this;
    }

    /**
     * Specifically validates the message shown after a successful account deletion.
     */
    @Step("Verify 'account deleted' message is displayed")
    public NotesAppLoginPage accountDeletedMessageShouldBeDisplayed() {
        assertThat(alertMessage()).isVisible();
        assertThat(alertMessage()).hasText("Your account has been deleted. You should create a new account to continue.");
        return this;
    }

    // --- Actions ---

    /**
     * Performs the login operation.
     * @param user The {@link User} record containing email and password credentials.
     * @return A new instance of NotesAppHomePage after successful redirection.
     */
    @Step("Login with email: {email}")
    public NotesAppHomePage login(User user) {
        emailInput().fill(user.email());
        passwordInput().fill(user.password());
        loginButton().click();
        page.waitForURL("**" + NOTES_HOME_PAGE);
        return new NotesAppHomePage(page);
    }
}