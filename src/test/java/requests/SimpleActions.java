package requests;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static constants.Constants.*;
import static requests.RequestLibrary.*;

public interface SimpleActions {

    @Step("Health check")
    static Response healthCheck() {
        return sendGetRequest(HEALTH_CHECK, "");
    }

    @Step("Register a user with record: {record}")
    static Response registerUser(Record record) {
        return sendPostRequest(record, REGISTER_USER);
    }

    @Step("Login a user with record: {record}")
    static Response loginUser(Record record) {
        return sendPostRequest(record, LOGIN_USER);
    }

    @Step("Get a user profile")
    static Response getUserProfile(String authToken) {
        return sendGetRequest(GET_USER_PROFILE, authToken);
    }

    @Step("Update a user profile with record: {record}")
    static Response updateUserProfile(Record record, String authToken) {
        return sendPatchRequest(record, UPDATE_USER_PROFILE, authToken);
    }

    @Step("Send reset password link with record: {record}")
    static Response sendPasswordResetLink(Record record) {
        return sendPostRequest(record, FORGOT_PASSWORD);
    }

    @Step("Change user's password")
    static Response changePassword(Record record, String authToken) {
        return sendPostRequest(record, CHANGE_PASSWORD, authToken);
    }

    @Step("Log out a user")
    static Response logout(String authToken) {
        return sendDeleteRequest(LOGOUT_USER, authToken);
    }

    @Step("Delete user's account")
    static Response deleteAccount(String authToken) {
        return sendDeleteRequest(DELETE_ACCOUNT, authToken);
    }

    @Step("Create a note with record: {record}")
    static Response createNote(Record record, String authToken) {
        return RequestLibrary.sendPostRequest(record, CREATE_NOTE, authToken);
    }

    @Step("Get all notes")
    static Response getNotes(String authToken) {
        return RequestLibrary.sendGetRequest(GET_NOTES, authToken);
    }

    @Step("Get a not by Id: {noteId}")
    static Response getNoteById(String noteId, String authToken) {
        return RequestLibrary.sendGetRequestWithParam(GET_NOTE_BY_ID, authToken, noteId);
    }

    @Step("Update a note by Id: {noteId} with record: {record}")
    static Response updateNote(String noteId, Record record, String authToken) {
        return RequestLibrary.sendPutRequestWithParam(record, UPDATE_NOTE, noteId, authToken);
    }

    @Step("Update note's complete status")
    static Response completeNote(String noteId, Record record, String authToken) {
        return RequestLibrary.sendPatchRequestWithParam(record, COMPLETE_NOTE, noteId, authToken);
    }

    @Step("Delete a note by Id: {noteId}")
    static Response deleteNote(String noteId, String authToken) {
        return RequestLibrary.sendDeleteRequestWithParam(DELETE_NOTE, noteId, authToken);
    }

}
