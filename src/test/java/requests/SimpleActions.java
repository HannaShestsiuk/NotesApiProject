package requests;

import classes.LoginUser;
import io.restassured.response.Response;

import static constants.ApiConstants.*;
import static requests.RequestLibrary.*;

public interface SimpleActions {
    static Response healthCheck() {
        return sendGetRequest(HEALTH_CHECK_URL);
    }

    static Response registerUser(Record record) {
        return sendPostRequest(record, REGISTER_USER_URL);
    }

    static Response loginUser(Record record) {
        return sendPostRequest(record, LOGIN_USER_URL);
    }

    static Response loginUser(LoginUser user) {
        return sendPostRequest(user, LOGIN_USER_URL);
    }

    static Response createNote(Record record, String authToken) {
        return sendPostRequestWithAuth(record, CREATE_NOTE_URL, authToken);
    }
}
