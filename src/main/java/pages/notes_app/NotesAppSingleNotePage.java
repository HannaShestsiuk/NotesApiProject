package pages.notes_app;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import enums.NoteCategory;
import io.qameta.allure.Step;
import pages.BasePage;
import pages.notes_app.components.NotesAppDeleteNoteModal;
import pages.notes_app.components.NotesAppEditNoteModal;
import records.NoteWithStatus;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Constants.NOTES_SINGLE_NOTE_PAGE;

public class NotesAppSingleNotePage extends BasePage {

    public NotesAppSingleNotePage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return NOTES_SINGLE_NOTE_PAGE + "[a-z0-9]+";
    }

    // --- Locators ---
    private final Locator title = page.getByTestId("note-card-title");
    private final Locator description = page.getByTestId("note-card-description");
    private final Locator completedCheckbox = page.getByTestId("toggle-note-switch");
    private final Locator editButton = page.getByTestId("note-edit");
    private final Locator deleteButton = page.getByTestId("note-delete");
    private final Locator loader = page.locator(".progress");

    // --- Assertions ---
    @Step("Verify single note page details match: {expectedNote.title}")
    public NotesAppSingleNotePage noteDetailsShouldMatch(NoteWithStatus expectedNote) {
        assertThat(title).hasText(expectedNote.title());
        assertThat(description).hasText(expectedNote.description());

        String expectedColor = NoteCategory.getColorByLabel(expectedNote.category(), expectedNote.completed());
        assertThat(title).hasCSS("background-color", expectedColor);

        if (expectedNote.completed()) {
            assertThat(completedCheckbox).isChecked();
        } else {
            assertThat(completedCheckbox).not().isChecked();
        }

        return this;
    }

    @Step("Set 'Completed' status to: {isCompleted}")
    public void setCompletedStatus(boolean isCompleted) {
        if (isCompleted) {
            completedCheckbox.check();
        } else {
            completedCheckbox.uncheck();
        }
    }

    @Step("Click 'Edit' from the single note page")
    public NotesAppEditNoteModal clickEdit() {
        editButton.click();
        return new NotesAppEditNoteModal(page);
    }

    @Step("Wait for home page loader to disappear")
    public NotesAppSingleNotePage waitForLoaderToDisappear() {
        assertThat(loader).isHidden();
        return this;
    }

    @Step("Click 'Delete' from the single note page")
    public NotesAppDeleteNoteModal clickDelete() {
        deleteButton.click();
        return new NotesAppDeleteNoteModal(page);
    }
}
