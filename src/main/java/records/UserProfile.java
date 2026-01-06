package records;

public record UserProfile(
        String name,
        String phone,
        String company
) {
    public UserProfile {
        name = name == null ? null : name.trim();
        phone = phone == null ? null : phone.trim();
        company = company == null ? null : company.trim();
    }
}
