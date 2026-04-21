package notes_app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import base.BasePage;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Constants.*;

/**
 * Page Object for the Notes App Welcome (Landing) page.
 * <p>
 * This is the entry point for the Notes App. It allows users to navigate to
 * Login, Registration, or Password Recovery sections.
 * </p>
 */
public class NotesAppWelcomePage extends BasePage {

    /**
     * Initializes the Notes App Welcome page.
     * @param page The Playwright Page instance.
     */
    public NotesAppWelcomePage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return NOTES_WELCOME_PAGE;
    }

    // --- Locators ---

    private Locator welcomeHeading() {
        return page.locator("h1");
    }

    private Locator loginButton() {
        return page.getByTestId("open-login-view");
    }

    private Locator createAccountButton() {
        return page.getByTestId("open-register-view");
    }

    // --- Assertions & Validations ---

    /**
     * Verifies that the Welcome page is correctly loaded by checking the URL,
     * the presence of the main heading, and the visibility of primary buttons.
     * @return This page instance for chaining.
     */
    @Step("Verify that Notes App Welcome page is loaded")
    public NotesAppWelcomePage welcomePageShouldBeOpened() {
        // Verify URL (using the constant provided in your navigation method)
        assertThat(page).hasURL(Pattern.compile(".*" + NOTES_WELCOME_PAGE));

        // Verify Heading
        assertThat(welcomeHeading()).hasText("Welcome to Notes App");

        // Verify Buttons are visible
        assertThat(loginButton()).isVisible();
        assertThat(createAccountButton()).isVisible();

        return this;
    }

    // --- Navigation Actions ---

    /**
     * Navigates the user to the Registration page.
     * @return A new instance of NotesAppRegisterPage.
     */
    @Step("Click on 'Create an account' button")
    public NotesAppRegisterPage clickCreateAccount() {
        createAccountButton().click();
        page.waitForURL("**" + NOTES_REGISTER_PAGE);
        return new NotesAppRegisterPage(page);
    }

    /**
     * Navigates the user to the Login page.
     * @return A new instance of NotesAppLoginPage.
     */
    @Step("Click on 'Login' button")
    public NotesAppLoginPage clickLogin() {
        loginButton().click();
        page.waitForURL("**" + NOTES_LOGIN_PAGE);
        return new NotesAppLoginPage(page);
    }
}