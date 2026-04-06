package notes_app.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import notes_app.BaseComponent;
import records.NoteWithStatus;

/**
 * Base class for Note-related modals (Add and Edit).
 * Encapsulates common fields like Title, Description, and Category.
 */
public abstract class BaseNoteModal extends BaseComponent {

    protected final Locator modalTitle = page.locator(".modal-title");
    protected final Locator categorySelect = page.getByTestId("note-category");
    protected final Locator completedCheckbox = page.getByTestId("note-completed");
    protected final Locator titleInput = page.getByTestId("note-title");
    protected final Locator descriptionInput = page.getByTestId("note-description");
    protected final Locator submitButton = page.getByTestId("note-submit");
    protected final Locator cancelButton = page.getByTestId("note-cancel");

    public BaseNoteModal(Page page) {
        super(page);
    }

    /**
     * Shared logic to fill the note form fields.
     */
    protected void fillNoteForm(NoteWithStatus note) {
        categorySelect.selectOption(note.category());

        // Ensure checkbox state matches the record
        if (note.completed()) {
            completedCheckbox.check();
        } else {
            completedCheckbox.uncheck();
        }

        titleInput.fill(note.title());
        descriptionInput.fill(note.description());
    }
}
