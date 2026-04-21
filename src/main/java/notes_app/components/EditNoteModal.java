package notes_app.components;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import notes_app.pages.NotesAppHomePage;
import records.NoteWithStatus;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Page Object for the Edit Note Modal.
 */
public class EditNoteModal extends BaseNoteModal {

    public EditNoteModal(Page page) {
        super(page);
    }

    @Step("Verify that 'Edit note' modal is visible")
    public EditNoteModal modalShouldBeVisible() {
        assertThat(modalTitle).hasText("Edit note");
        return this;
    }

    /**
     * Updates the note fields and saves changes.
     * @param note The updated note data.
     * @return The Home Page instance.
     */
    @Step("Update note details and save")
    public NotesAppHomePage updateNote(NoteWithStatus note) {
        categorySelect.selectOption(note.category());

        if (note.completed()) {
            completedCheckbox.check();
        } else {
            completedCheckbox.uncheck();
        }

        titleInput.fill(note.title());
        descriptionInput.fill(note.description());
        submitButton.click();

        return new NotesAppHomePage(page);
    }
}
