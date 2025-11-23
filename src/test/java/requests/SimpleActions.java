package requests;

import io.restassured.response.Response;

import static requests.ApiConstants.*;
import static requests.RequestLibrary.*;

public interface SimpleActions {
    static Response healthCheck() {
        return sendGetRequest(HEALTH_CHECK);
    }

    static Response registerUser(Record record) {
        return sendPostRequest(record, REGISTER_USER);
    }

    static Response loginUser(Record record) {
        return sendPostRequest(record, LOGIN_USER);
    }

    String authToken = "";

    static Response createNote(Record record, String authToken) {
        return sendPostRequestWithAuth(record, CREATE_NOTE, authToken);
    }
}
