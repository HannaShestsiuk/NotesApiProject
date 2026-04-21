package notes_app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import base.BasePage;
import records.User;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Constants.NOTES_LOGIN_PAGE;
import static constants.Constants.NOTES_REGISTER_PAGE;

/**
 * Page Object for the Notes App Registration page.
 * Handles user account creation and the transition to the success state.
 */
public class NotesAppRegisterPage extends BasePage {

    public NotesAppRegisterPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return NOTES_REGISTER_PAGE;
    }

    // --- Locators ---
    private Locator registerHeading() {
        return page.locator("h1");
    }

    private Locator emailInput() {
        return page.getByTestId("register-email");
    }

    private Locator nameInput() {
        return page.getByTestId("register-name");
    }

    private Locator passwordInput() {
        return page.getByTestId("register-password");
    }

    private Locator confirmPasswordInput() {
        return page.getByTestId("register-confirm-password");
    }

    private Locator registerButton() {
        return page.getByTestId("register-submit");
    }

    // Success State Locators
    private Locator successAlert() {
        return page.locator(".alert-success");
    }
    private Locator loginLinkAfterSuccess() {
        return page.getByTestId("login-view");
    }

    // --- Assertions & Validations ---

    @Step("Verify that Notes App Register page is loaded")
    public NotesAppRegisterPage registerPageShouldBeOpened() {
        assertThat(page).hasURL(Pattern.compile(".*" + NOTES_REGISTER_PAGE));
        assertThat(registerHeading()).hasText("Register");
        return this;
    }

    /**
     * Verifies the success message appears after form submission.
     * The URL remains the same during this state.
     */
    @Step("Verify success message is displayed")
    public NotesAppRegisterPage successMessageShouldBeDisplayed() {
        assertThat(successAlert()).isVisible();
        assertThat(successAlert()).containsText("User account created successfully");
        return this;
    }

    // --- Actions ---

    /**
     * Fills and submits the registration form.
     */
    @Step("Register a new user with email: {email}")
    public NotesAppRegisterPage register(User user) {
        emailInput().fill(user.email());
        nameInput().fill(user.userName());
        passwordInput().fill(user.password());
        confirmPasswordInput().fill(user.password());
        registerButton().click();
        return this;
    }

    /**
     * Clicks the login link provided in the success alert and waits for navigation.
     */
    @Step("'Click here to Log In' link after successful registration")
    public NotesAppLoginPage clickLoginLink() {
        loginLinkAfterSuccess().click();
        page.waitForURL("**" + NOTES_LOGIN_PAGE);
        return new NotesAppLoginPage(page);
    }
}