package pages.notes_app;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import records.Note;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Represents the Modal component for creating or editing notes in the Notes App.
 * <p>
 * This component provides methods to interact with the note form fields, including
 * category selection, completion status, title, and description.
 * </p>
 */
public class NotesAppNoteModal extends BaseComponent{

    public NotesAppNoteModal(Page page) {
        super(page);
    }

    private final Locator modalTitle = page.locator(".modal-title");
    private final Locator categorySelect = page.getByTestId("note-category");
    private final Locator completedCheckbox = page.getByTestId("note-completed");
    private final Locator titleInput = page.getByTestId("note-title");
    private final Locator descriptionInput = page.getByTestId("note-description");
    private final Locator createButton = page.getByTestId("note-submit");
    private final Locator cancelButton = page.getByTestId("note-cancel");

    /**
     * Constructs a new NotesAppNoteModal instance.
     * * @param page The Playwright Page object used to interact with the browser.
     */
    @Step("Verify that 'Add new note' modal is visible")
    public NotesAppNoteModal modalShouldBeVisible() {
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
     * @param isCompleted Boolean flag to determine if the 'Completed' checkbox should be checked.
     * @return A new instance of {@link NotesAppHomePage} representing the dashboard after submission.
     */
    @Step("Fill and submit note form: {note.title}")
    public NotesAppHomePage createNote(Note note, boolean isCompleted) {
        categorySelect.selectOption(note.category());
        assertThat(categorySelect).hasValue(note.category());

        if (isCompleted) {
            completedCheckbox.check();
        }
        titleInput.fill(note.title());
        descriptionInput.fill(note.description());
        createButton.click();

        return new NotesAppHomePage(page);
    }
}
