package pages.notes_app;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.BasePage;
import pages.notes_app.components.NotesAppAddNoteModal;
import pages.notes_app.components.NotesAppNoteCardComponent;

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
        return page.getByTestId("home");
    }

    private Locator profileButton() {
        return page.getByTestId("profile");
    }

    private Locator logoutButton() {
        return page.getByTestId("logout");
    }

    private Locator addNoteButton() {
        return page.getByTestId("add-new-note");
    }

    private final Locator searchInput = page.getByTestId("search-input");
    private final Locator searchButton = page.getByTestId("search-btn");

    private Locator categoryAllButton() {
        return page.getByTestId("category-all");
    }

    private Locator categoryHomeButton() {
        return page.getByTestId("category-home");
    }

    private Locator categoryWorkButton() {
        return page.getByTestId("category-work");
    }

    private Locator categoryPersonalButton() {
        return page.getByTestId("category-personal");
    }

    private final Locator progressInfo = page.getByTestId("progress-info");

    private final Locator loader = page.locator(".progress");
    public final Locator noteCards = page.getByTestId("note-card");

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
    public NotesAppWelcomePage userLogout() {
        logoutButton().click();
        page.waitForURL("**" + NOTES_WELCOME_PAGE);
        return new NotesAppWelcomePage(page);
    }

    /**
     * Verifies that the search results header is displayed and contains the expected query string.
     *
     * @param query The search term expected to appear in the results message.
     * @return This {@link NotesAppHomePage} instance for method chaining.
     */
    @Step("Verify search results message contains: {query}")
    public NotesAppHomePage searchResultsShouldBeVisible(String query) {
        assertThat(page.locator("p >> text=Search Results for")).containsText(query);
        return this;
    }

    /**
     * Asserts that the number of note cards currently visible on the dashboard
     * matches the expected count.
     *
     * @param count The expected number of visible notes.
     * @return This {@link NotesAppHomePage} instance for method chaining.
     */
    @Step("Verify that exactly {count} notes are displayed")
    public NotesAppHomePage countOfNotesShouldBe(int count) {
        assertThat(noteCards).hasCount(count);
        return this;
    }

    /**
     * Performs a search operation by filling the search input and clicking the search button.
     *
     * @param query The string to search for within the notes.
     * @return This {@link NotesAppHomePage} instance for method chaining.
     */
    @Step("Search for notes with query: {query}")
    public NotesAppHomePage searchNotes(String query) {
        searchInput.fill(query);
        searchButton.click();
        return this;
    }

    /**
     * Clears the current text in the search input field and triggers a search
     * to reset the view to show all notes.
     *
     * @return This {@link NotesAppHomePage} instance for method chaining.
     */
    @Step("Clear search input")
    public NotesAppHomePage clearSearch() {
        searchInput.clear();
        searchButton.click();
        return this;
    }

    /**
     * Filters notes by the 'All' category.
     */
    @Step("Filter notes by category: All")
    public NotesAppHomePage filterByAll() {
        categoryAllButton().click();
        return this;
    }

    /**
     * Filters notes by the 'Home' category.
     */
    @Step("Filter notes by category: Home")
    public NotesAppHomePage filterByHome() {
        categoryHomeButton().click();
        return this;
    }

    /**
     * Filters notes by the 'Work' category.
     */
    @Step("Filter notes by category: Work")
    public NotesAppHomePage filterByWork() {
        categoryWorkButton().click();
        return this;
    }

    /**
     * Filters notes by the 'Personal' category.
     */
    @Step("Filter notes by category: Personal")
    public NotesAppHomePage filterByPersonal() {
        categoryPersonalButton().click();
        return this;
    }

    /**
     * Verifies the progress info text matches the expected category and count.
     * * @param expectedText The text expected in the info bar (e.g., "work category").
     * @return This {@link NotesAppHomePage} instance.
     */
    @Step("Verify progress info displays: {expectedText}")
    public NotesAppHomePage progressInfoShouldContain(String expectedText) {
        assertThat(progressInfo).containsText(expectedText);
        return this;
    }

    @Step("Click on '+ Add Note' button")
    public NotesAppAddNoteModal clickAddNote() {
        addNoteButton().click();
        return new NotesAppAddNoteModal(page);
    }

    @Step("Wait for home page loader to disappear")
    public NotesAppHomePage waitForLoaderToDisappear() {
        assertThat(loader).isHidden();
        return this;
    }

    /**
     * Helper to wrap a specific card locator into a NoteCardComponent.
     */
    public NotesAppNoteCardComponent getNoteCardByTitle(String title) {
        Locator note = noteCards.filter(new Locator.FilterOptions()
                .setHas(page.getByTestId("note-card-title")
                        .getByText(title, new Locator.GetByTextOptions().setExact(true))));
        return new NotesAppNoteCardComponent(page, note);
    }
}