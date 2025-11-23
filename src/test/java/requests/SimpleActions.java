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
}
