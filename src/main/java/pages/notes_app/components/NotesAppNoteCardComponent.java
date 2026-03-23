package pages.notes_app.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import enums.NoteCategory;
import io.qameta.allure.Step;
import pages.notes_app.BaseComponent;
import pages.notes_app.NotesAppSingleNotePage;
import records.NoteWithStatus;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Represents a single Note Card component on the Home Page.
 */
public class NotesAppNoteCardComponent extends BaseComponent {
    private final Locator title;
    private final Locator description;
    private final Locator completedCheckbox;
    private final Locator viewButton;
    private final Locator editButton;
    private final Locator deleteButton;

    public NotesAppNoteCardComponent(Page page, Locator note) {
        super(page);
        this.title = note.getByTestId("note-card-title");
        this.description = note.getByTestId("note-card-description");
        this.completedCheckbox = note.getByTestId("toggle-note-switch");
        this.viewButton = note.getByTestId("note-view");
        this.editButton = note.getByTestId("note-edit");
        this.deleteButton = note.getByTestId("note-delete");
    }

    @Step("Verify note card details")
    public void noteDetailsShouldMatch(NoteWithStatus expectedNote) {
        assertThat(title).hasText(expectedNote.title());
        assertThat(description).hasText(expectedNote.description());

        String expectedColor = NoteCategory.getColorByLabel(expectedNote.category(), expectedNote.completed());
        assertThat(title).hasCSS("background-color", expectedColor);

        if (expectedNote.completed()) assertThat(completedCheckbox).isChecked();
        else assertThat(completedCheckbox).not().isChecked();
    }

    @Step("Set 'Completed' status to: {isCompleted}")
    public void setCompletedStatus(boolean isCompleted) {
        if (isCompleted) {
            completedCheckbox.check();
        } else {
            completedCheckbox.uncheck();
        }
    }

    @Step("Click 'View' on this note card")
    public NotesAppSingleNotePage clickViewButton() {
        viewButton.click();
        return new NotesAppSingleNotePage(page);
    }

    @Step("Click 'Edit' on this note card")
    public void clickEditButton() {
        editButton.click();
    }

    @Step("Click 'Delete' on this note card")
    public void clickDeleteButton() {
        deleteButton.click();
    }
}
