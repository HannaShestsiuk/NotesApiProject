package api_tests.Notes;

import api_tests.BaseApiTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.Note;
import records.NoteStatus;
import requests.SimpleActions;

import java.util.stream.Stream;

import static classes.TestDataGenerator.randomDescription;
import static classes.TestDataGenerator.randomTitle;
import static constants.ApiConstants.BASE_SCHEMA;
import static constants.ApiConstants.NOTE_PATCH_STATUS_SCHEMA;
import static constants.Messages.*;
import static enums.NoteCategory.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.TestUtils.assertResponseSchema;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class NotePatchStatusTest extends BaseApiTest {

    private static Stream<Arguments> validPatchProvider() {
        return Stream.of(
                Arguments.of("Mark note as completed", true),
                Arguments.of("Mark note as not completed", false)
        );
    }

    private static Stream<Arguments> invalidPatchProvider() {
        return Stream.of(
                Arguments.of(
                        "No Auth",
                        "69483980294a09029728a4bd",
                        new NoteStatus(true),
                        "",
                        NO_AUTH_HEADER,
                        401
                ),
                Arguments.of(
                        "Invalid ID",
                        "invalid-id",
                        new NoteStatus(true),
                        authToken,
                        NOTE_INVALID_ID,
                        400
                )
        );
    }

    @DisplayName("[API. Notes]. PATCH Method. Update Note's completed status")
    @Description("""
            1. Create a note.
            2. Update note status
            3. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
    @MethodSource("validPatchProvider")
    void patchNoteStatusPositiveTests(
            String description,
            boolean newStatus
    ) {
        Note original = new Note(
                randomTitle(),
                randomDescription(),
                HOME.getLabel()
        );

        Response createResponse = SimpleActions.createNote(original, authToken);
        String noteId = createResponse.jsonPath().getString("data.id");

        NoteStatus statusUpdate = new NoteStatus(newStatus);
        Response patchResponse = SimpleActions.completeNote(noteId, statusUpdate, authToken);

        assertResponseSchema(NOTE_PATCH_STATUS_SCHEMA, patchResponse);

        assertAll(description,
                () -> assertEquals(NOTE_UPDATED, patchResponse.jsonPath().getString("message")),
                () -> assertEquals(200, patchResponse.jsonPath().getInt("status")),
                () -> assertTrue(patchResponse.jsonPath().getBoolean("success")),
                () -> assertEquals(newStatus, patchResponse.jsonPath().getBoolean("data.completed")),
                () -> assertEquals(original.title(), patchResponse.jsonPath().getString("data.title")),
                () -> assertEquals(original.description(), patchResponse.jsonPath().getString("data.description")),
                () -> assertEquals(original.category(), patchResponse.jsonPath().getString("data.category")),
                () -> assertEquals(userId, patchResponse.jsonPath().getString("data.user_id"))
        );
    }

    @DisplayName("[API. Notes]. PATCH Method. Update Note's completed status")
    @Description("""
            1. Update Note Status.
            2. Assert the response.
            """)
    @ParameterizedTest(name = "with {0}")
    @MethodSource("invalidPatchProvider")
    void patchNoteStatusNegativeTests(
            String description,
            String noteId,
            NoteStatus statusUpdate,
            String token,
            String expectedMessage,
            int expectedStatus
    ) {
        Response response = SimpleActions.completeNote(noteId, statusUpdate, token);

        assertResponseSchema(BASE_SCHEMA, response);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message")),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }
}

