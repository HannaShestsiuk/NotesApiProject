package api_tests.Notes;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.Note;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.randomDescription;
import static classes.TestDataGenerator.randomTitle;
import static constants.Messages.*;
import static enums.NoteCategory.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteGetByIdTest extends BaseApiTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("validNoteByIdProvider")
    void getNoteByIdPositiveTests(String description, Note note) {

        Response createResponse = SimpleActions.createNote(note, authToken);
        String noteId = createResponse.jsonPath().getString("data.id");

        Response response = SimpleActions.getNoteById(noteId, authToken);

        assertAll(description,
                () -> assertEquals(NOTE_RETRIEVED, response.jsonPath().getString("message")),
                () -> assertEquals(200, response.jsonPath().getInt("status")),
                () -> assertTrue(response.jsonPath().getBoolean("success")),
                () -> assertEquals(note.title(), response.jsonPath().getString("data.title")),
                () -> assertEquals(note.description(), response.jsonPath().getString("data.description")),
                () -> assertEquals(note.category(), response.jsonPath().getString("data.category")),
                () -> assertEquals(userId, response.jsonPath().getString("data.user_id"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidNoteByIdProvider")
    void getNoteByIdNegativeTests(
            String description,
            String noteId,
            String token,
            String expectedMessage,
            int expectedStatus
    ) {
        Response response = SimpleActions.getNoteById(noteId, token);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message")),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    private Stream<Arguments> validNoteByIdProvider() {
        return Stream.of(
                Arguments.of(
                        "Valid note by ID",
                        new Note(
                                randomTitle(),
                                randomDescription(),
                                HOME.getLabel()
                        )
                )
        );
    }

    private Stream<Arguments> invalidNoteByIdProvider() {
        return Stream.of(
                Arguments.of(
                        "No Auth",
                        "12345",
                        "",
                        NO_AUTH_HEADER,
                        401
                ),
                Arguments.of(
                        "Invalid ID format",
                        "invalid-id",
                        authToken,
                        NOTE_INVALID_ID,
                        400
                ),
                Arguments.of(
                        "Non-existing ID",
                        "694820b1294a090297281241",
                        authToken,
                        NOTE_NOT_FOUND,
                        404
                )
        );
    }
}

