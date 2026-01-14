package api_tests.Notes;

import api_tests.BaseApiTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.Note;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.randomDescription;
import static classes.TestDataGenerator.randomTitle;
import static constants.ApiConstants.BASE_SCHEMA;
import static constants.Messages.*;
import static enums.NoteCategory.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.TestUtils.assertResponseSchema;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteDeleteTest extends BaseApiTest {

    {
        enableGlobalUser = true;
    }

    private static Stream<Arguments> validDeleteProvider() {
        return Stream.of(
                Arguments.of(
                        "Delete existing note",
                        new Note(
                                randomTitle(),
                                randomDescription(),
                                HOME.getLabel()
                        )
                )
        );
    }

    private static Stream<Arguments> invalidDeleteProvider() {
        return Stream.of(
                Arguments.of(
                        "No Auth",
                        "69483cee294a09029728a55f",
                        "",
                        NO_AUTH_HEADER,
                        401
                )                ,
                Arguments.of(
                        "Non existing Note",
                        "69483cee294a09029728a111",
                        "USE_VALID_TOKEN",
                        NOTE_NOT_FOUND,
                        404
                ),
                Arguments.of(
                        "Invalid ID format",
                        "invalid-id",
                        "USE_VALID_TOKEN",
                        NOTE_INVALID_ID,
                        400
                )
        );
    }

    @DisplayName("[API. Notes]. DELETE Method. Delete a Note")
    @Description("""
            1. Create a note.
            2. Delete the note.
            3. Assert the response.
            4. Get the deleted note.
            5. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
    @MethodSource("validDeleteProvider")
    void deleteNotePositiveTests(String description, Note note) {

        Response createResponse = SimpleActions.createNote(note, authToken);

        registerNoteId(createResponse);

        String noteId = createResponse.jsonPath().getString("data.id");

        Response deleteResponse = SimpleActions.deleteNote(noteId, authToken);

        assertResponseSchema(BASE_SCHEMA, deleteResponse);

        assertAll(description,
                () -> assertEquals(NOTE_DELETED, deleteResponse.jsonPath().getString("message")),
                () -> assertEquals(200, deleteResponse.jsonPath().getInt("status")),
                () -> assertTrue(deleteResponse.jsonPath().getBoolean("success"))
        );

        Response getResponse = SimpleActions.getNoteById(noteId, authToken);

        assertAll("Verify note is deleted",
                () -> assertEquals(NOTE_NOT_FOUND, getResponse.jsonPath().getString("message")),
                () -> assertEquals(404, getResponse.jsonPath().getInt("status")),
                () -> assertFalse(getResponse.jsonPath().getBoolean("success"))
        );
    }

    @DisplayName("[API. Notes]. DELETE Method. Delete a Note")
    @Description("""
            1. Delete the note.
            2. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
    @MethodSource("invalidDeleteProvider")
    void deleteNoteNegativeTests(
            String description,
            String noteId,
            String token,
            String expectedMessage,
            int expectedStatus
    ) {
        // Replace placeholders
        String tokenToUse = token.equals("USE_VALID_TOKEN") ? authToken : token;

        Response response = SimpleActions.deleteNote(noteId, tokenToUse);

        assertResponseSchema(BASE_SCHEMA, response);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message")),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }
}

