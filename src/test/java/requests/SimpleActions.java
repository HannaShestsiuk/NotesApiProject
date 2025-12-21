package requests;

import io.restassured.response.Response;

import static constants.ApiConstants.*;
import static requests.RequestLibrary.*;

public interface SimpleActions {
    static Response healthCheck() {
        return sendGetRequest(HEALTH_CHECK, "");
    }

    static Response registerUser(Record record) {
        return sendPostRequest(record, REGISTER_USER);
    }

    static Response loginUser(Record record) {
        return sendPostRequest(record, LOGIN_USER);
    }

    static Response getUserProfile(String authToken) {
        return sendGetRequest(GET_USER_PROFILE, authToken);
    }

    static Response updateUserProfile(Record record, String authToken) {
        return sendPatchRequest(record, UPDATE_USER_PROFILE, authToken);
    }

    static Response sendPasswordResetLink(Record record) {
        return sendPostRequest(record, FORGOT_PASSWORD);
    }

    static Response changePassword(Record record, String authToken) {
        return sendPostRequest(record, CHANGE_PASSWORD, authToken);
    }

    static Response logout(String authToken) {
        return sendDeleteRequest(LOGOUT_USER, authToken);
    }

    static Response deleteAccount(String authToken) {
        return sendDeleteRequest(DELETE_ACCOUNT, authToken);
    }

    static Response createNote(Record record, String authToken) {
        return RequestLibrary.sendPostRequest(record, CREATE_NOTE, authToken);
    }

    static Response getNotes(String authToken) {
        return RequestLibrary.sendGetRequest(GET_NOTES, authToken);
    }

    static Response getNoteById(String noteId, String authToken) {
        return RequestLibrary.sendGetRequestWithParam(GET_NOTE_BY_ID, authToken, noteId);
    }

    static Response updateNote(String noteId, Record record, String authToken) {
        return RequestLibrary.sendPutRequestWithParam(record, UPDATE_NOTE, noteId, authToken);
    }

    static Response completeNote(String noteId, Record record, String authToken) {
        return RequestLibrary.sendPatchRequestWithParam(record, COMPLETE_NOTE, noteId, authToken);
    }

    static Response deleteNote(String noteId, String authToken) {
        return RequestLibrary.sendDeleteRequestWithParam(DELETE_NOTE, noteId, authToken);
    }

}
