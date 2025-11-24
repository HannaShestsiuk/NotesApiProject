package records;

public enum Messages {
    ACCOUNT_CREATED("User account created successfully"),
    UNIQUE_EMAIL_REQUIRED("An account already exists with the same email address"),
    VALID_EMAIL_REQUIRED("A valid email address is required"),
    VALID_USERNAME_REQUIRED("User name must be between 4 and 30 characters"),
    VALID_PASSWORD_REQUIRED("Password must be between 6 and 30 characters");

    private final String label;

    Messages(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
