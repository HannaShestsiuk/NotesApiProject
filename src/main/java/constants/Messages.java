package constants;

public final class Messages {
    private Messages() {
    }

    // NoteApp API tests messages

    public static final String HEALTH_CHECK = "Notes API is Running";
    public static final String ACCOUNT_CREATED = "User account created successfully";
    public static final String UNIQUE_EMAIL_REQUIRED = "An account already exists with the same email address";
    public static final String VALID_EMAIL_REQUIRED = "A valid email address is required";
    public static final String VALID_USERNAME_REQUIRED = "User userName must be between 4 and 30 characters";
    public static final String VALID_PASSWORD_REQUIRED = "Password must be between 6 and 30 characters";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGIN_INVALID_EMAIL_OR_PASSWORD = "Incorrect email address or password";
    public static final String NO_AUTH_HEADER = "No authentication token specified in x-auth-token header";
    public static final String USER_PROFILE = "Profile successful";
    public static final String USER_PROFILE_UPDATED = "Profile updated successful";
    public static final String INVALID_PHONE = "Phone number should be between 8 and 20 digits";
    public static final String INVALID_COMPANY = "Company userName must be between 4 and 30 characters";
    public static final String PASSWORD_RESET_LINK_SENT = "Password reset link successfully sent to %s. Please verify by clicking on the given link";

    public static String passwordResetLinkSent(String email) {
        return String.format(PASSWORD_RESET_LINK_SENT, email);
    }

    public static final String PASSWORD_CHANGED ="The password was successfully updated";
    public static final String CURRENT_PASSWORD_INCORRECT = "The current password is incorrect";
    public static final String VALID_CURRENT_PASSWORD_REQUIRED = "Current password must be between 6 and 30 characters";
    public static final String VALID_NEW_PASSWORD_REQUIRED = "New password must be between 6 and 30 characters";
    public static final String NEW_AND_CURRENT_PASSWORDS_EQUAL = "The new password should be different from the current password";
    public static final String NO_ACCOUNT_WITH_EMAIL = "No account found with the given email address";
    public static final String USER_LOGOUT = "User has been successfully logged out";
    public static final String ACCOUNT_DELETED = "Account successfully deleted";
    public static final String NOTE_CREATED = "Note successfully created";
    public static final String NOTE_INVALID_TITLE = "Title must be between 4 and 100 characters";
    public static final String NOTE_INVALID_DESCRIPTION = "Description must be between 4 and 1000 characters";
    public static final String NOTE_INVALID_CATEGORY = "Category must be one of the categories: Home, Work, Personal";
    public static final String NOTES_RETRIEVED = "Notes successfully retrieved";
    public static final String NOTE_RETRIEVED = "Note successfully retrieved";
    public static final String NOTE_NOT_FOUND = "No note was found with the provided ID, Maybe it was deleted";
    public static final String NOTE_INVALID_ID = "Note ID must be a valid ID";
    public static final String NOTE_UPDATED = "Note successfully Updated";
    public static final String NOTE_DELETED = "Note successfully deleted";
    public static final String INVALID_REQUEST = "Invalid Request";

    // Practice UI messages

    public static final String SUCCESSFUL_REGISTRATION = "Successfully registered, you can log in now.";
    public static final String USERNAME_IS_TAKEN = "Username is already taken.";
    public static final String FIELDS_REQUIRED = "All fields are required.";
    public static final String PASSWORDS_NOT_MATCH = "Passwords do not match.";
    public static final String REGISTRATION_ERROR = "An error occurred during registration. Please try again.";
    public static final String SUCCESSFUL_LOGIN = "You logged into a secure area!";
    public static final String LOGIN_INVALID_USERNAME = "Your userName is invalid!";
    public static final String LOGIN_INVALID_PASSWORD = "Your password is invalid!";
    public static final String LOGOUT_MESSAGE = "You logged out of the secure area!";
    public static final String PASSWORD_RESET_SENT = "An e-mail has been sent to you which explains how to reset your password.";
    public static final String INVALID_EMAIL = "Please enter a valid email address.";

}
