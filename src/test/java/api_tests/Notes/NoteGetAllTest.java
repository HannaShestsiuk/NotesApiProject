package api_tests.Notes;

import api_tests.BaseApiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import records.Note;
import requests.SimpleActions;

import java.util.List;
import java.util.stream.Stream;

import static classes.TestDataGenerator.randomDescription;
import static classes.TestDataGenerator.randomTitle;
import static constants.Messages.NOTES_RETRIEVED;
import static constants.Messages.NO_AUTH_HEADER;
import static enums.NoteCategory.*;
import static junit.framework.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteGetAllTest extends BaseApiTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("notesListProvider")
    void getNotesPositiveTests(String description, List<Note> notesToCreate) {

        for (Note note : notesToCreate) {
            SimpleActions.createNote(note, authToken);
        }

        Response response = SimpleActions.getNotes(authToken);

        assertAll(description,
                () -> assertEquals(NOTES_RETRIEVED, response.jsonPath().getString("message")),
                () -> assertEquals(200, response.jsonPath().getInt("status")),
                () -> assertTrue(response.jsonPath().getBoolean("success"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidGetNotesProvider")
    void getNotesNegativeTests(
            String description,
            String token,
            String expectedMessage,
            int expectedStatus
    ) {
        Response response = SimpleActions.getNotes(token);

        assertAll(description,
                () -> assertEquals(expectedMessage, response.jsonPath().getString("message")),
                () -> assertEquals(expectedStatus, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    private static Stream<Arguments> invalidGetNotesProvider() {
        return Stream.of(
                Arguments.of("No Auth", "", NO_AUTH_HEADER, 401)
        );
    }


    private Stream<Arguments> notesListProvider() {
        return Stream.of(
                Arguments.of(
                        "User with multiple notes",
                        List.of(
                                new Note(randomTitle(), randomDescription(), HOME.getLabel()),
                                new Note(randomTitle(), randomDescription(), WORK.getLabel()),
                                new Note(randomTitle(), randomDescription(), PERSONAL.getLabel())
                        )
                )
        );
    }
}
