package api_tests.Notes;

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
import static org.junit.jupiter.api.Assertions.*;
import static enums.NoteCategory.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteCreationTest extends BaseApiTest {

    private Stream<Arguments> validNoteProvider(){
        return Stream.of(
            Arguments.of("Note with valid values", new Note(
                    randomTitle(),
                    randomDescription(),
                    HOME.getLabel())
            ),
            Arguments.of("Title Length = MIN(4)", new Note(
                    randomTitle(4),
                    randomDescription(),
                    WORK.getLabel())
            ),
            Arguments.of("Title Length = MAX(100)", new Note(
                    randomTitle(100),
                    randomDescription(),
                    PERSONAL.getLabel())
            ),
            Arguments.of("Description Length = MIN(4)", new Note(
                    randomTitle(),
                    randomDescription(4),
                    WORK.getLabel())
            ),
            Arguments.of("Description Length = MAX(1000)", new Note(
                    randomTitle(),
                    randomDescription(1000),
                    HOME.getLabel())
            )
        );
    }

    static Stream<Arguments> invalidNoteProvider() {
        return Stream.of(
                Arguments.of("No Auth",
                        new Note(randomTitle(),
                                randomDescription(),
                                HOME.getLabel()),
                        "",
                        NO_AUTH_HEADER,
                        401),
                Arguments.of("Missing title",
                        new Note(null,
                                randomDescription(),
                                HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_TITLE,
                        400),
                Arguments.of("Empty title",
                        new Note("",
                                randomDescription(),
                                HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_TITLE,
                        400),
                Arguments.of("Title Length < MIN(4)",
                        new Note(randomTitle(3),
                                randomDescription(),
                                HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_TITLE,
                        400),
                Arguments.of("Title Length > MAX(100)",
                        new Note(randomTitle(101),
                                randomDescription(),
                                HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_TITLE,
                        400),
                Arguments.of("Missing description",
                        new Note(randomTitle(),
                                null,
                                HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_DESCRIPTION,
                        400),
                Arguments.of("Empty description",
                        new Note(randomTitle(),
                                "",
                                HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_DESCRIPTION,
                        400),
                Arguments.of("Description Length < MIN(4)",
                        new Note(randomTitle(),
                                randomDescription(3),
                                HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_DESCRIPTION,
                        400),
                Arguments.of("Description Length > MAX(1000)",
                        new Note(randomTitle(),
                                randomDescription(1001),
                                HOME.getLabel()),
                        authToken,
                        NOTE_INVALID_DESCRIPTION,
                        400),
                Arguments.of(
                        "Missing category",
                        new Note(randomTitle(),
                                randomDescription(),
                                null),
                        authToken,
                        NOTE_INVALID_CATEGORY,
                        400
                ),
                Arguments.of(
                        "Invalid category",
                        new Note(randomTitle(),
                                randomDescription(),
                                "invalid"),
                        authToken,
                        NOTE_INVALID_CATEGORY,
                        400
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validNoteProvider")
    void createNewNotePositiveTests(String description, Note note) {
        Response response = SimpleActions.createNote(note, authToken);

        assertAll(description,
                () -> assertEquals(NOTE_CREATED, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertTrue(response.jsonPath().getBoolean("success"), "Invalid success status."),
                () -> assertEquals(note.title(), response.jsonPath().getString("data.title"), "Invalid note title."),
                () -> assertEquals(note.description(), response.jsonPath().getString("data.description"), "Invalid note description."),
                () -> assertEquals(note.category(), response.jsonPath().getString("data.category"), "Invalid note category."),
                () -> assertFalse(response.jsonPath().getBoolean("data.completed"), "Invalid note completion date."),
                () -> assertEquals(userId, response.jsonPath().getString("data.user_id"), "Invalid user id.")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidNoteProvider")
    void createNoteNegativeTests(String description,
                                 Note note,
                                 String token,
                                 String expectedMessage,
                                 int expectedStatus) {

        Response response = SimpleActions.createNote(note, token);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message"), "Invalid message."),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status"), "Invalid Status Code."),
                () -> assertFalse(response.jsonPath().getBoolean("success"), "Invalid success status.")
        );
    }
}
