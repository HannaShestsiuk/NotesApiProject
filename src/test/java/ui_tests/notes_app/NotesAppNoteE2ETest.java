package ui_tests.notes_app;

import enums.NoteCategory;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.notes_app.NotesAppHomePage;
import pages.notes_app.NotesAppLoginPage;
import records.Note;
import records.User;
import testdata.TestUsers;
import ui_tests.BaseTest;

import static helpers.TestDataGenerator.randomDescription;
import static helpers.TestDataGenerator.randomTitle;

public class NotesAppNoteE2ETest extends BaseTest {
    @Test
    @DisplayName("[UI]. Notes App. Note Lifecycle: Create -> Verify -> Delete")
    @Description("""
            1. Login to the Notes App using a pre-registered user.
            2. Create a new note with a dynamic title to avoid data collision.
            3. Wait for the page loader to disappear to ensure UI stability.
            4. Validate the Note Card: Title, Description, and Category-specific background color.
            5. Verify the 'Not Completed' status via the checkbox.
            6. Delete the note via the Delete Confirmation Modal.
            7. Verify the note is removed from the dashboard.
            """)
    void userNoteLifecycleTest() {
        User testUser = TestUsers.notesAppUiUser();

        Note myNote = new Note(
                randomTitle(5),
                randomDescription(),
                NoteCategory.random().getLabel()
        );

        NotesAppLoginPage loginPage = new NotesAppLoginPage(page());
        loginPage.open();

        NotesAppHomePage notesHome = loginPage.login(testUser)
                .homePageShouldBeOpened();

        notesHome.clickAddNote()
                .modalShouldBeVisible()
                .createNote(myNote, false); // isCompleted = false

        notesHome.waitForLoaderToDisappear()
                .noteShouldBeVisible(myNote.title())
                .noteDetailsShouldMatch(myNote, false);

        notesHome.clickDeleteNote(myNote.title())
                .modalShouldBeVisible()
                .confirmDeletion();

        notesHome.noteShouldBeDeleted(myNote.title());
    }
}
