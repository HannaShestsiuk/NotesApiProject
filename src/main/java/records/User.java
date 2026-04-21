package records;

public record User(
        String userName,
        String email,
        String password
) {
    public User {
        userName = userName == null ? null : userName.trim();
        email = email == null ? null : email.trim();
    }
}
