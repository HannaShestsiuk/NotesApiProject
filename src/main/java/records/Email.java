package records;

public record Email(
        String email
) {
    public Email {
        email = email == null ? null : email.trim();
    }
}
