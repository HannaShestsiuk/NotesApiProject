package ui_tests.notes_app;

import enums.NoteCategory;
import helpers.TestDataGenerator;
import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import notes_app.pages.NotesAppHomePage;
import notes_app.pages.NotesAppLoginPage;
import notes_app.pages.NotesAppSinglePage;
import notes_app.components.NoteCardComponent;
import records.NoteWithStatus;
import records.User;
import testdata.TestUsers;
import ui_tests.BaseTest;

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

        NoteCardComponent myNote = notesHome
                .waitForLoaderToDisappear()
                .getNoteCardByTitle(note.title());

        myNote.shouldBeVisible()
                .noteDetailsShouldMatch(note);

        myNote.clickDeleteButton()
                .modalShouldBeVisible()
                .confirmDeletion();

        myNote.shouldBeDeleted();
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

        NoteCardComponent myNote = notesHome.waitForLoaderToDisappear()
                .getNoteCardByTitle(note.title());

        myNote.shouldBeVisible()
                .noteDetailsShouldMatch(note);

        myNote.clickEditButton()
                .updateNote(updatedNote);

        myNote = notesHome.waitForLoaderToDisappear()
                .getNoteCardByTitle(updatedNote.title())
                .noteDetailsShouldMatch(updatedNote);

        myNote.clickDeleteButton()
                .modalShouldBeVisible()
                .confirmDeletion();

        myNote.shouldBeDeleted();
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

        NoteCardComponent myNote = notesHome
                .waitForLoaderToDisappear()
                .getNoteCardByTitle(note.title());

        myNote.shouldBeVisible()
                .noteDetailsShouldMatch(note);

        NotesAppSinglePage singleNotePage = myNote.clickViewButton();

        singleNotePage.noteDetailsShouldMatch(note);

        singleNotePage.clickEdit()
                .modalShouldBeVisible()
                .updateNote(updatedNote);

        singleNotePage.waitForLoaderToDisappear()
                .noteDetailsShouldMatch(updatedNote);

        singleNotePage.clickDelete()
                .modalShouldBeVisible()
                .confirmDeletion();

        myNote.shouldBeDeleted();
    }

    @Test
    @DisplayName("[UI]. Notes App. Bulk Operations for  multiple notes: Create -> Delete")
    @Description("""
            1. Login to the application with a pre-registered user.
            2. Generate a dynamic list of 10 Note objects.
            3. Perform a bulk creation of all notes in the list.
            4. Perform a bulk verification to ensure every note card matches the expected data (Title, Description, Color).
            5. Execute a UI-driven bulk deletion of all visible notes.
            6. Verify the dashboard is empty.
            7. Log the user out to clean up the session.
        """)
    void bulkNoteOperationsTest() {

        List<NoteWithStatus> notesList = TestDataGenerator.generateRandomNotes(10);

        notesHome.bulkCreateNotes(notesList)
                .bulkVerifyNotesDetails(notesList)
                .bulkDeleteAllVisibleNotes();

        notesHome.countOfNotesShouldBe(0);
    }

    @Test
    @DisplayName("[UI]. Notes App. Search: Create -> Filter -> Delete All")
    @Description("""
    1. Create 4 notes with randomized data.
    2. Search by the first note's title and verify it is the only one visible.
    3. Delete the filtered note from the search results.
    4. Clear the search to reveal the remaining 3 notes.
    5. Iterate through the remaining notes and delete them to leave a clean state.
    """)
    void searchNoteByTitleTest() {
        List<NoteWithStatus> notesList = TestDataGenerator.generateRandomNotes(4);
        notesHome.bulkCreateNotes(notesList);

        String searchKeyword = notesList.getFirst().title();
        notesHome.searchNotes(searchKeyword)
                .waitForLoaderToDisappear()
                .searchResultsShouldBeVisible(searchKeyword)
                .countOfNotesShouldBe(1);

        notesHome.getNoteCardByTitle(searchKeyword)
                .shouldBeVisible()
                .noteDetailsShouldMatch(notesList.getFirst())
                .clickDeleteButton()
                .confirmDeletion();

        notesHome.waitForLoaderToDisappear()
                .countOfNotesShouldBe(0)
                .clearSearch()
                .filterByAll()
                .waitForLoaderToDisappear();

        notesHome.bulkDeleteAllVisibleNotes();

        notesHome.countOfNotesShouldBe(0)
                .noNotesMessageShouldBeVisible();
    }

    @Test
    @DisplayName("[UI]. Notes App. Category Filtering: Create -> Filter -> Delete All")
    @Description("""
    1. Create 3 notes in different categories (Home, Work, Personal).
    2. Sequentially apply each category filter.
    3. Verify that for each filter, only the matching note is visible and details are correct.
    4. Reset filter to 'All' and perform a bulk deletion.
    """)
    void filterNotesByCategoryTest() {
        NoteWithStatus homeNote = new NoteWithStatus(randomTitle(5), randomDescription(), false, NoteCategory.HOME.getLabel());
        NoteWithStatus workNote = new NoteWithStatus(randomTitle(5), randomDescription(), false, NoteCategory.WORK.getLabel());
        NoteWithStatus personalNote = new NoteWithStatus(randomTitle(5), randomDescription(), false, NoteCategory.PERSONAL.getLabel());

        List<NoteWithStatus> allNotes = List.of(homeNote, workNote, personalNote);

        notesHome.bulkCreateNotes(allNotes)
                .bulkVerifyCategoryFiltering(allNotes)
                .filterByAll()
                .waitForLoaderToDisappear()
                .bulkDeleteAllVisibleNotes();

        notesHome.countOfNotesShouldBe(0)
                .noNotesMessageShouldBeVisible();
    }
}
