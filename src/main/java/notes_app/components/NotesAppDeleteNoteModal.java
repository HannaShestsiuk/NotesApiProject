package notes_app.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import notes_app.BaseComponent;
import notes_app.pages.NotesAppHomePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NotesAppDeleteNoteModal extends BaseComponent {
    public NotesAppDeleteNoteModal(Page page) {
        super(page);
    }

    private final Locator dialog = page.getByTestId("note-delete-dialog");
    private final Locator modalTitle = dialog.locator(".modal-title");
    private final Locator confirmDeleteButton = page.getByTestId("note-delete-confirm");
    private final Locator cancelButton = page.getByTestId("note-delete-cancel-2");



    @Step("Verify delete confirmation modal is visible")
    public NotesAppDeleteNoteModal modalShouldBeVisible() {
        assertThat(modalTitle).hasText("Delete note?");
        return this;
    }

    @Step("Confirm deletion of the note")
    public NotesAppHomePage confirmDeletion() {
        confirmDeleteButton.click();
        return new NotesAppHomePage(page);
    }
}
