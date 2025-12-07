package constants;

public final class Messages {
    private Messages() {
    }

    public static final String HEALTH_CHECK = "Notes API is Running";
    public static final String ACCOUNT_CREATED = "User account created successfully";
    public static final String UNIQUE_EMAIL_REQUIRED = "An account already exists with the same email address";
    public static final String VALID_EMAIL_REQUIRED = "A valid email address is required";
    public static final String VALID_USERNAME_REQUIRED = "User name must be between 4 and 30 characters";
    public static final String VALID_PASSWORD_REQUIRED = "Password must be between 6 and 30 characters";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGIN_INVALID_EMAIL_OR_PASSWORD = "Incorrect email address or password";
    public static final String NO_AUTH_HEADER = "No authentication token specified in x-auth-token header";
    public static final String USER_PROFILE = "Profile successful";
    public static final String USER_PROFILE_UPDATED = "Profile updated successful";
    public static final String INVALID_PHONE = "Phone number should be between 8 and 20 digits";
    public static final String INVALID_COMPANY = "Company name must be between 4 and 30 characters";
    public static final String PASSWORD_RESET_LINK_SENT = "Password reset link successfully sent to %s. Please verify by clicking on the given link";

    public static String passwordResetLinkSent(String email) {
        return String.format(PASSWORD_RESET_LINK_SENT, email);
    }

    public static final String NO_ACCOUNT_WITH_EMAIL = "No account found with the given email address";
    public static final String NOTE_CREATED = "Note successfully created";
    public static final String NOTE_INVALID_TITLE = "Title must be between 4 and 100 characters";
    public static final String NOTE_INVALID_DESCRIPTION = "Description must be between 4 and 1000 characters";
    public static final String NOTE_INVALID_CATEGORY = "Category must be one of the categories: Home, Work, Personal";
    public static final String INVALID_REQUEST = "Invalid Request";
}
