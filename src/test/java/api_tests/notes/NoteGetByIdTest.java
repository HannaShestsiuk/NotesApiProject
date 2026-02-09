package api_tests.notes;

import api_tests.BaseApiTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.Note;
import requests.SimpleActions;

import java.util.stream.Stream;

import static constants.Messages.*;
import static enums.NoteCategory.*;
import static helpers.TestDataGenerator.randomDescription;
import static helpers.TestDataGenerator.randomTitle;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteGetByIdTest extends BaseApiTest {

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

    @DisplayName("[API. Notes]. GET Method. GET a Note by Id")
    @Description("""
            1. Create a note.
            2. Get created note
            3. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
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

    @DisplayName("[API. Notes]. GET Method. GET a Note by Id")
    @Description("""
            1. Create a note.
            2. Get a note
            3. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
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
}

