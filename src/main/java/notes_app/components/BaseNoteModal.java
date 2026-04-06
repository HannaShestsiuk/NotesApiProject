package notes_app.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import records.NoteWithStatus;

/**
 * Base class for Note-related modals (Add and Edit).
 * Encapsulates common fields like Title, Description, and Category.
 */
public abstract class BaseNoteModal {

    protected final Page page;

    protected final Locator modalTitle;
    protected final Locator categorySelect;
    protected final Locator completedCheckbox;
    protected final Locator titleInput;
    protected final Locator descriptionInput;
    protected final Locator submitButton;
    protected final Locator cancelButton;

    public BaseNoteModal(Page page) {
        this.page = page;
        this.modalTitle = page.locator(".modal-title");
        this.categorySelect = page.getByTestId("note-category");
        this.completedCheckbox = page.getByTestId("note-completed");
        this.titleInput = page.getByTestId("note-title");
        this.descriptionInput = page.getByTestId("note-description");
        this.submitButton = page.getByTestId("note-submit");
        this.cancelButton = page.getByTestId("note-cancel");
    }

    /**
     * Shared logic to fill the note form fields.
     */
    protected void fillNoteForm(NoteWithStatus note) {
        categorySelect.selectOption(note.category());

        if (note.completed()) {
            completedCheckbox.check();
        } else {
            completedCheckbox.uncheck();
        }

        titleInput.fill(note.title());
        descriptionInput.fill(note.description());
    }
}
