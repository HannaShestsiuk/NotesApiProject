package notes_app.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import notes_app.pages.NotesAppHomePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NotesAppDeleteNoteModal {

    private final Page page;

    private final Locator dialog;
    private final Locator modalTitle;
    private final Locator confirmDeleteButton;
    private final Locator cancelButton;

    public NotesAppDeleteNoteModal(Page page) {

        this.page = page;

        this.dialog = page.getByTestId("note-delete-dialog");
        this.modalTitle = dialog.locator(".modal-title");
        this.confirmDeleteButton = page.getByTestId("note-delete-confirm");
        this.cancelButton = page.getByTestId("note-delete-cancel-2");
    }

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
