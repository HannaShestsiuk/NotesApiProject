package ui_tests.notes_app;

import enums.NoteCategory;
import helpers.TestDataGenerator;
import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.notes_app.NotesAppHomePage;
import pages.notes_app.NotesAppLoginPage;
import pages.notes_app.NotesAppSingleNotePage;
import records.NoteWithStatus;
import records.User;
import testdata.TestUsers;
import ui_tests.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static helpers.TestDataGenerator.randomDescription;
import static helpers.TestDataGenerator.randomTitle;

public class NotesAppTest extends BaseTest {

    private NotesAppHomePage notesHome;
    private final User testUser = TestUsers.notesAppUiUser();

    @BeforeEach
    void userLogin() {
        NotesAppLoginPage loginPage = new NotesAppLoginPage(page());
        loginPage.open();

        notesHome = loginPage.login(testUser)
                .homePageShouldBeOpened();
    }

    @AfterEach
    void userLogout() {
        if (notesHome != null) {
            notesHome.userLogout();
        }
    }

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
            8. Log the user out.
            """)
    void createAndDeleteNoteTest() {

        NoteWithStatus note = new NoteWithStatus(
                randomTitle(5),
                randomDescription(),
                false,
                NoteCategory.random().getLabel()
        );

        notesHome.clickAddNote()
                .modalShouldBeVisible()
                .createNote(note);

        notesHome.waitForLoaderToDisappear()
                .noteShouldBeVisible(note.title())
                .noteDetailsShouldMatch(note);

        notesHome.deleteNote(note.title())
                .modalShouldBeVisible()
                .confirmDeletion();

        notesHome.noteShouldBeDeleted(note.title());
    }

    @Test
    @DisplayName("[UI]. Notes App. Note Lifecycle: Create -> Edit -> Delete")
    @Description("""
        1. Login to the Notes App using a pre-registered user.
        2. Create a new note with dynamic data (Category, Title, Description).
        3. Validate the initial Note Card: Title, Description, Color, and 'Incomplete' status.
        4. Open the 'Edit Note' modal and update all fields, including marking it as 'Completed'.
        5. Verify the Note Card reflects the updated data and the 'Completed' background color.
        6. Delete the note via the Delete Confirmation Modal.
        7. Verify the note is successfully removed from the dashboard.
        8. Log the user out to clean up the session.
    """)
    void createEditAndDeleteNoteTest() {

        NoteWithStatus note = new NoteWithStatus(
                randomTitle(5),
                randomDescription(),
                false,
                NoteCategory.random().getLabel()
        );

        NoteWithStatus updatedNote = new NoteWithStatus(
                randomTitle(5),
                randomDescription(),
                true,
                NoteCategory.random().getLabel()
        );

        notesHome.clickAddNote()
                .modalShouldBeVisible()
                .createNote(note);

        notesHome.waitForLoaderToDisappear()
                .noteShouldBeVisible(note.title())
                .noteDetailsShouldMatch(note);

        notesHome.editNote(note.title())
                .modalShouldBeVisible()
                .updateNote(updatedNote);

        notesHome.waitForLoaderToDisappear()
                .noteShouldBeVisible(updatedNote.title())
                .noteDetailsShouldMatch(updatedNote);

        notesHome.deleteNote(updatedNote.title())
                .modalShouldBeVisible()
                .confirmDeletion();

        notesHome.noteShouldBeDeleted(updatedNote.title());
    }

    @Test
    @DisplayName("[UI]. Notes App. Note Lifecycle: Create -> View -> Edit -> Delete")
    @Description("""
        1. Login to the Notes App using a pre-registered user.
        2. Create a new note with dynamic data (Category, Title, Description).
        3. Navigate to the Single Note Page to verify details in the expanded view.
        4. Open the 'Edit Note' modal from the Single Note Page and update all fields, including 'Completed' status.
        5. Verify the Single Note Page reflects the updated data and the new 'Completed' background color.
        6. Initiate deletion from the Single Note Page and confirm via the modal.
        7. Verify the user is redirected to the Home Page and the note is no longer visible.
        8. Log the user out to clean up the session.
    """)
    void createViewEditAndDeleteNoteTest() {

        NoteWithStatus note = new NoteWithStatus(
                randomTitle(5),
                randomDescription(),
                false,
                NoteCategory.random().getLabel()
        );

        NoteWithStatus updatedNote = new NoteWithStatus(
                randomTitle(5),
                randomDescription(),
                true,
                NoteCategory.random().getLabel()
        );

        notesHome.clickAddNote()
                .modalShouldBeVisible()
                .createNote(note);

        NotesAppHomePage noteCard = notesHome.waitForLoaderToDisappear()
                .noteShouldBeVisible(note.title());

        NotesAppSingleNotePage singleNotePage = noteCard.viewNote(note.title());

        singleNotePage.noteDetailsShouldMatch(note);

        singleNotePage.clickEdit()
                .modalShouldBeVisible()
                .updateNote(updatedNote);

        singleNotePage.waitForLoaderToDisappear()
                .noteDetailsShouldMatch(updatedNote);

        notesHome = singleNotePage.clickDelete()
                .modalShouldBeVisible()
                .confirmDeletion();

        notesHome.noteShouldBeDeleted(updatedNote.title());
    }

    @Test
    @DisplayName("[UI]. Notes App. Bulk Operations for  multiple notes: Create -> Delete")
    @Description("""
        1. Login to the application with a pre-registered user.
        2. Generate a dynamic list of 10 Note objects.
        3. Iterate through the list to Create each note and verify its immediate visibility.
        4. Iterate through the list to Delete each note via the confirmation modal.
        5. Verify each note is successfully removed.
        6. Perform a clean logout to terminate the session.
        """)
    void bulkNoteOperationsTest() {

        List<NoteWithStatus> notesList = TestDataGenerator.generateRandomNotes(10);

        notesList.forEach(note -> {
            notesHome.clickAddNote()
                    .modalShouldBeVisible()
                    .createNote(note);
            notesHome.waitForLoaderToDisappear()
                    .noteShouldBeVisible(note.title());
        });

        notesList.forEach(note -> {
            notesHome.deleteNote(note.title())
                    .modalShouldBeVisible()
                    .confirmDeletion();
            notesHome.waitForLoaderToDisappear()
                    .noteShouldBeDeleted(note.title());
        });
    }
}
