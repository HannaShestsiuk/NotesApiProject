package api_tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import records.Note;
import requests.SimpleActions;

import static enums.Messages.*;
import static org.junit.jupiter.api.Assertions.*;
import static enums.NoteCategory.*;


public class NoteCreationTest extends BaseApiTest {

    @Test
    void createNewNoteTest () {
        Note note = new Note(
                "Note Title " + System.currentTimeMillis(),
                "This note should be created",
                HOME.getLabel()
        );

        Response response = SimpleActions.createNote(note, authToken);

        assertAll("Note is created by registered user",
                () -> assertEquals(NOTE_CREATED.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status."),
                () -> assertEquals(note.title(), response.jsonPath().getString("data.title"), "Invalid note title."),
                () -> assertEquals(note.description(), response.jsonPath().getString("data.description"), "Invalid note description."),
                () -> assertEquals(note.category(), response.jsonPath().getString("data.category"), "Invalid note category."),
                () -> assertFalse(response.jsonPath().getBoolean("data.completed"), "Invalid note completion date."),
                () -> assertEquals(userId, response.jsonPath().getString("data.user_id"), "Invalid user id.")
        );
    }

    @Test
    void createNoteWithoutAuthTest() {
        Note note = new Note(
                "Note Title " + System.currentTimeMillis(),
                "Unregistered user is NOT able to create a note",
                HOME.getLabel()
        );

        String authToken = "";
        Response response = SimpleActions.createNote(note, authToken);

        assertAll("Note is NOT created by non-registered user",
                () -> assertEquals(NO_AUTH_HEADER.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(401, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void createNoteWithoutTitleTest() {
        Note note = new Note(
                null,
                "This note should NOT be created",
                HOME.getLabel()
        );

        Response response = SimpleActions.createNote(note, authToken);

        assertAll("Note is NOT created without title",
                () -> assertEquals(NOTE_INVALID_TITLE.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void createNoteWithInvalidTitleTest() {
        Note note = new Note(
                "Ups",
                "This note should NOT be created",
                HOME.getLabel()
        );

        Response response = SimpleActions.createNote(note, authToken);

        assertAll("Note is NOT created with invalid title",
                () -> assertEquals(NOTE_INVALID_TITLE.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void createNoteWithoutDescriptionTest() {
        Note note = new Note(
                "Note Title " + System.currentTimeMillis(),
                null,
                HOME.getLabel()
        );

        Response response = SimpleActions.createNote(note, authToken);

        assertAll("Note is NOT created without description",
                () -> assertEquals(NOTE_INVALID_DESCRIPTION.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void createNoteWithInvalidDescriptionTest() {
        Note note = new Note(
                "Note Title " + System.currentTimeMillis(),
                "foo",
                HOME.getLabel()
        );

        Response response = SimpleActions.createNote(note, authToken);

        assertAll("Note is NOT created with invalid description",
                () -> assertEquals(NOTE_INVALID_DESCRIPTION.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void createNoteWithoutCategoryTest() {
        Note note = new Note(
                "Note Title " + System.currentTimeMillis(),
                "Note without category",
                null
        );

        Response response = SimpleActions.createNote(note, authToken);

        assertAll("Note is NOT created without category",
                () -> assertEquals(NOTE_INVALID_CATEGORY.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }

    @Test
    void createNoteWithInvalidCategoryTest() {
        Note note = new Note(
                "Note Title " + System.currentTimeMillis(),
                "Note without category",
                "NoHome"
        );

        Response response = SimpleActions.createNote(note, authToken);

        assertAll("Note is NOT created with invalid category",
                () -> assertEquals(NOTE_INVALID_CATEGORY.getLabel(), response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(400, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }
}
