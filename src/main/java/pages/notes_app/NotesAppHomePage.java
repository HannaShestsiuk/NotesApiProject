package pages.notes_app;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.BasePage;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Constants.NOTES_HOME_PAGE;
import static constants.Constants.NOTES_PROFILE_PAGE;
import static constants.Constants.NOTES_WELCOME_PAGE;

/**
 * Page Object for the Notes App Home Dashboard.
 * <p>
 * This is the landing page after a successful login, displaying the
 * user's notes and providing access to their profile.
 * </p>
 */
public class NotesAppHomePage extends BasePage {

    /**
     * Initializes the Notes App Home page.
     * @param page The Playwright Page instance.
     */
    public NotesAppHomePage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return NOTES_HOME_PAGE;
    }

    // --- Locators ---

    private Locator myNotesTitle() {
        // Targets the brand link in the navbar
        return page.getByTestId("home");
    }

    private Locator profileButton() {
        return page.getByTestId("profile");
    }

    private Locator logoutButton() {
        return page.getByTestId("logout");
    }

    // --- Assertions & Validations ---

    /**
     * Verifies that the Home page is correctly loaded.
     * @return This page instance.
     */
    @Step("Verify that Notes App Home page is loaded")
    public NotesAppHomePage homePageShouldBeOpened() {
        assertThat(page).hasURL(Pattern.compile(".*" + NOTES_HOME_PAGE));
        assertThat(myNotesTitle()).containsText("MyNotes");
        assertThat(profileButton()).isVisible();
        assertThat(logoutButton()).isVisible();
        return this;
    }

    // --- Actions ---

    /**
     * Navigates the user to the Profile page.
     * @return A new instance of NotesAppProfilePage.
     */
    @Step("Click on 'Profile' button")
    public NotesAppProfilePage goToProfile() {
        profileButton().click();
        page.waitForURL("**" + NOTES_PROFILE_PAGE);
        return new NotesAppProfilePage(page);
    }

    /**
     * Logs the user out and returns to the Welcome page.
     * @return A new instance of NotesAppWelcomePage.
     */
    @Step("Click on 'Logout' button")
    public NotesAppWelcomePage logout() {
        logoutButton().click();
        page.waitForURL("**" + NOTES_WELCOME_PAGE);
        return new NotesAppWelcomePage(page);
    }
}