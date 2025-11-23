package requests;

import io.restassured.response.Response;

import static notesApi.ApiConstants.REGISTER_USER;
import static requests.RequestLibrary.sendPostRequest;

public interface SimpleActions {
    static Response registerUser(Record record) {
        return sendPostRequest(record, REGISTER_USER);
    }
}
