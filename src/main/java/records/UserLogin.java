package records;

public record UserLogin(
        String email,
        String password) {
    public UserLogin {
        email = email == null ? null : email.trim();
    }
}
