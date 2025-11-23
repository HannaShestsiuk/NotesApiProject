package records;

public record RegisteredUser(
        String email,
        String password) {
    public RegisteredUser {
        email = email == null ? null : email.trim();
    }
}
