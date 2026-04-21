package notes_app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import enums.NoteCategory;
import io.qameta.allure.Step;
import base.BasePage;
import notes_app.components.DeleteNoteModal;
import notes_app.components.EditNoteModal;
import records.NoteWithStatus;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Constants.NOTES_SINGLE_NOTE_PAGE;

public class NotesAppSinglePage extends BasePage {

    public NotesAppSinglePage(Page page) {
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
    public NotesAppSinglePage noteDetailsShouldMatch(NoteWithStatus expectedNote) {
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
    public EditNoteModal clickEdit() {
        editButton.click();
        return new EditNoteModal(page);
    }

    @Step("Wait for home page loader to disappear")
    public NotesAppSinglePage waitForLoaderToDisappear() {
        assertThat(loader).isHidden();
        return this;
    }

    @Step("Click 'Delete' from the single note page")
    public DeleteNoteModal clickDelete() {
        deleteButton.click();
        return new DeleteNoteModal(page);
    }
}
