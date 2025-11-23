package notesApi;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import records.Note;
import records.NoteCategory;
import records.RegisteredUser;
import records.User;
import requests.SimpleActions;

import static org.junit.jupiter.api.Assertions.*;

public class NoteCreationTest {
    @Test
    void noteIsNotCreatedByNotRegisteredUser() {
        Note note = new Note(
                "Note Title " + System.currentTimeMillis(),
                "Unregistered user is NOT able to create a note",
                "Home"
        );

        Response response = SimpleActions.createNote(note, SimpleActions.authToken);

        assertAll("Note is NOT created by non-registered user",
                () -> assertEquals("No authentication token specified in x-auth-token header", response.jsonPath().getString("message")),
                () -> assertEquals(401, response.jsonPath().getInt("status")),
                () -> assertFalse(response.jsonPath().getBoolean("success"))
        );
    }

    private static String authToken = "";
    private static String userId = "";

    @BeforeAll
    static void userRegistrationAndLogin() {
        User user = new User(
                "Test User",
                System.currentTimeMillis() + "@mail.com",
                "Strong123!"
        );

        Response registerUserResponse = SimpleActions.registerUser(user);

        assertAll("Successful user registration response validation",
                () -> assertEquals(201, registerUserResponse.jsonPath().getInt("status")),
                () -> assertTrue(registerUserResponse.jsonPath().getBoolean("success"))
        );

        RegisteredUser registeredUser = new RegisteredUser(
                user.email(),
                user.password()
        );

        Response loginResponse = SimpleActions.loginUser(registeredUser);

        assertAll("Registered user is logged in",
                () -> assertEquals(200, loginResponse.jsonPath().getInt("status")),
                () -> assertTrue(loginResponse.jsonPath().getBoolean("success"))
        );

        authToken = loginResponse.jsonPath().getString("data.token");
        userId = loginResponse.jsonPath().getString("data.id");
    }

    @Test
    void noteIsCreatedByRegisteredUser() {
        Note note = new Note(
                "Note Title " + System.currentTimeMillis(),
                "This note should be created",
                NoteCategory.HOME.getLabel()
        );

        Response response = SimpleActions.createNote(note, authToken);

        assertAll("Note is NOT created by non-registered user",
                () -> assertEquals("Note successfully created", response.jsonPath().getString("message")),
                () -> assertEquals(200, response.jsonPath().getInt("status")),
                () -> assertTrue(response.jsonPath().getBoolean("success")),
                () -> assertEquals(note.title(), response.jsonPath().getString("data.title")),
                () -> assertEquals(note.description(), response.jsonPath().getString("data.description")),
                () -> assertEquals(note.category(), response.jsonPath().getString("data.category")),
                () -> assertFalse(response.jsonPath().getBoolean("data.completed")),
                () -> assertEquals(userId, response.jsonPath().getString("data.user_id"))
        );
    }
}
