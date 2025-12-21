package api_tests.Notes;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.Note;
import records.NoteWithStatus;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.randomDescription;
import static classes.TestDataGenerator.randomTitle;
import static constants.Messages.*;
import static enums.NoteCategory.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteUpdateTest extends BaseApiTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("validUpdateProvider")
    void updateNotePositiveTests(String description, Note originalNote, NoteWithStatus updatedNote) {

        Response createResponse = SimpleActions.createNote(originalNote, authToken);
        String noteId = createResponse.jsonPath().getString("data.id");

        Response updateResponse = SimpleActions.updateNote(noteId, updatedNote, authToken);

        assertAll(description,
                () -> assertEquals(NOTE_UPDATED, updateResponse.jsonPath().getString("message")),
                () -> assertEquals(200, updateResponse.jsonPath().getInt("status")),
                () -> assertTrue(updateResponse.jsonPath().getBoolean("success")),
                () -> assertEquals(updatedNote.title(), updateResponse.jsonPath().getString("data.title")),
                () -> assertEquals(updatedNote.description(), updateResponse.jsonPath().getString("data.description")),
                () -> assertEquals(updatedNote.category(), updateResponse.jsonPath().getString("data.category")),
                () -> assertEquals(updatedNote.completed(), updateResponse.jsonPath().getBoolean("data.completed")),
                () -> assertEquals(userId, updateResponse.jsonPath().getString("data.user_id"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidUpdateWithExistingNoteProvider")
    void updateNoteNegativeTestsExistingNote(
            String description,
            NoteWithStatus updatedNote,
            String authToken,
            String expectedMessage,
            int expectedStatus
    ) {
        Note originalNote = new Note(
                randomTitle(),
                randomDescription(),
                HOME.getLabel()
        );

        Response createResponse = SimpleActions.createNote(originalNote, authToken);
        String noteId = createResponse.jsonPath().getString("data.id");

        Response response = SimpleActions.updateNote(noteId, updatedNote, authToken);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message")),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidUpdateNoNoteProvider")
    void updateNoteNegativeTestsNoExistingNote(
            String description,
            String noteId,
            NoteWithStatus updatedNote,
            String authToken,
            String expectedMessage,
            int expectedStatus
    ) {
        Response response = SimpleActions.updateNote(noteId, updatedNote, authToken);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message")),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }


    private Stream<Arguments> validUpdateProvider() {
        return Stream.of(
                Arguments.of(
                        "Update all fields",
                        new Note(randomTitle(), randomDescription(), HOME.getLabel()),
                        new NoteWithStatus(
                                randomTitle(),
                                randomDescription(),
                                true,
                                WORK.getLabel()
                        )
                )
        );
    }

    private Stream<Arguments> invalidUpdateWithExistingNoteProvider() {
        return Stream.of(
                Arguments.of(
                        "No Auth",
                        new NoteWithStatus("New Title", "New Desc", true, HOME.getLabel()),
                        "",
                        NO_AUTH_HEADER,
                        401
                ),
                Arguments.of(
                        "Missing title",
                        new NoteWithStatus(null, "Valid Desc", true, HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_TITLE,
                        400
                ),
                Arguments.of(
                        "Missing description",
                        new NoteWithStatus("Valid Title", null, true, HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_DESCRIPTION,
                        400
                )
        );
    }

    private Stream<Arguments> invalidUpdateNoNoteProvider() {
        return Stream.of(
                Arguments.of(
                        "Non existing Note",
                        "69483547294a09029728a111",
                        new NoteWithStatus("New Title", "New Desc", true, HOME.getLabel()),
                        authToken,
                        NOTE_NOT_FOUND,
                        404
                )
        );
    }
}