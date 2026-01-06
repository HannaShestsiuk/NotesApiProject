package records;

public record User(
        String name,
        String email,
        String password
) {
    public User {
        name = name == null ? null : name.trim();
        email = email == null ? null : email.trim();
    }
}
