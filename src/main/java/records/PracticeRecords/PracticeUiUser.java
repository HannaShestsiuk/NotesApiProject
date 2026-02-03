package records.PracticeRecords;

public record PracticeUiUser(
        String name,
        String password,
        String confirmPassword
) {
    public PracticeUiUser(String name, String password, String confirmPassword) {
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }


    @Override
    public String toString() {
        return name;
    }
}
