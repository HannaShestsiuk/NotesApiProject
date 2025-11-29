package enums;

public enum Messages {
    ACCOUNT_CREATED("User account created successfully"),
    UNIQUE_EMAIL_REQUIRED("An account already exists with the same email address"),
    VALID_EMAIL_REQUIRED("A valid email address is required"),
    VALID_USERNAME_REQUIRED("User name must be between 4 and 30 characters"),
    VALID_PASSWORD_REQUIRED("Password must be between 6 and 30 characters"),
    LOGIN_SUCCESS("Login successful"),
    LOGIN_INVALID_EMAIL_OR_PASSWORD("Incorrect email address or password"),
    NO_AUTH_HEADER("No authentication token specified in x-auth-token header"),
    NOTE_CREATED("Note successfully created"),
    NOTE_INVALID_TITLE("Title must be between 4 and 100 characters"),
    NOTE_INVALID_DESCRIPTION("Description must be between 4 and 1000 characters"),
    NOTE_INVALID_CATEGORY("Category must be one of the categories: Home, Work, Personal");


    private final String label;

    Messages(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
