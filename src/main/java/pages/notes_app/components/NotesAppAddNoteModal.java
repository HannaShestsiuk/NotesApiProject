package pages.notes_app.components;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.notes_app.NotesAppHomePage;
import records.Note;
import records.NoteWithStatus;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Represents the Modal component for creating or editing notes in the Notes App.
 * <p>
 * This component provides methods to interact with the note form fields, including
 * category selection, completion status, title, and description.
 * </p>
 */
public class NotesAppAddNoteModal extends BaseNoteModal {

    public NotesAppAddNoteModal(Page page) {
        super(page);
    }

    /**
     * Constructs a new NotesAppNoteModal instance.
     * * @param page The Playwright Page object used to interact with the browser.
     */
    @Step("Verify that 'Add new note' modal is visible")
    public NotesAppAddNoteModal modalShouldBeVisible() {
        assertThat(modalTitle).hasText("Add new note");
        return this;
    }

    /**
     * Fills out the note creation form and submits it.
     * <p>
     * This method selects the category, optionally marks the note as completed,
     * fills in the text fields, and clicks the submit button.
     * </p>
     * * @param note        The {@link Note} record containing the title, description, and category.
     * @return A new instance of {@link NotesAppHomePage} representing the dashboard after submission.
     */
    @Step("Fill and submit note form: {note.title}")
    public NotesAppHomePage createNote(NoteWithStatus note) {
        fillNoteForm(note);
        submitButton.click();
        return new NotesAppHomePage(page);
    }
}
