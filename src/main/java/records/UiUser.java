package records;

public record UiUser(
        String email,
        String name,
        String password,
        String confirmPassword
) {
    public UiUser {
        name = name == null ? null : name.trim();
        email = email == null ? null : email.trim();
    }
}
