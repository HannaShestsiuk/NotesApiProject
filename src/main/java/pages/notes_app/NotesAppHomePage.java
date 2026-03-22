package pages.notes_app;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.BasePage;
import pages.notes_app.components.NotesAppAddNoteModal;
import pages.notes_app.components.NotesAppDeleteNoteModal;
import pages.notes_app.components.NotesAppEditNoteModal;
import pages.notes_app.components.NotesAppNoteCardComponent;
import records.NoteWithStatus;

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
    private Locator noteTitleList() {
        return page.getByTestId("note-card-title");
    }
    private final Locator loader = page.locator(".progress");
    private final Locator noteCards = page.getByTestId("note-card");

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
    private NotesAppNoteCardComponent getNoteCardByTitle(String title) {
        Locator note = noteCards.filter(new Locator.FilterOptions()
                .setHas(page.getByTestId("note-card-title")
                        .getByText(title, new Locator.GetByTextOptions().setExact(true))));
        return new NotesAppNoteCardComponent(page, note);
    }

    @Step("Verify that a note with title '{title}' is visible on the page")
    public NotesAppHomePage noteShouldBeVisible(String title) {
        Locator targetCard = noteCards.filter(new Locator.FilterOptions().setHasText(title));
        assertThat(targetCard).isVisible();
        return this;
    }

    /**
     * Finds a note card by title and validates all its properties.
     */
    @Step("Verify note details for title: {expectedNote.title}")
    public NotesAppHomePage noteDetailsShouldMatch(NoteWithStatus expectedNote) {
        getNoteCardByTitle(expectedNote.title()).noteDetailsShouldMatch(expectedNote);
        return this;
    }

    @Step("Click edit button for note: {title}")
    public NotesAppSingleNotePage viewNote(String title) {
        getNoteCardByTitle(title).clickViewButton();
        return new NotesAppSingleNotePage(page);
    }

    /**
     * Opens the Edit Modal for a specific note.
     * * @param title The current title of the note to be edited.
     * @return A new instance of NotesAppEditNoteModal.
     */
    @Step("Click edit button for note: {title}")
    public NotesAppEditNoteModal editNote(String title) {
        getNoteCardByTitle(title).clickEditButton();
        return new NotesAppEditNoteModal(page);
    }

    /**
     * Initiates the deletion process for a specific note.
     * <p>
     * This method locates the note card containing the specified title and clicks
     * the delete button within that specific card. This triggers the confirmation modal.
     * </p>
     * @param title The title of the note to be deleted, used to identify the correct card.
     * @return A new instance of {@link NotesAppDeleteNoteModal} to handle the confirmation dialog.
     */
    @Step("Click delete button for note: {title}")
    public NotesAppDeleteNoteModal deleteNote(String title) {
        getNoteCardByTitle(title).clickDeleteButton();
        return new NotesAppDeleteNoteModal(page);
    }

    @Step("Verify that note with title '{title}' is no longer visible")
    public NotesAppHomePage noteShouldBeDeleted(String title) {
        Locator targetCard = noteCards.filter(new Locator.FilterOptions().setHasText(title));
        assertThat(targetCard).not().isVisible();
        return this;
    }
}