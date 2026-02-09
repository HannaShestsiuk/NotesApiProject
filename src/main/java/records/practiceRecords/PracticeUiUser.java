package records.practiceRecords;

public record PracticeUiUser(
        String userName,
        String password,
        String confirmPassword
) {
    public PracticeUiUser(String userName, String password, String confirmPassword) {
        this.userName = userName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Override
    public String toString() {
        return userName;
    }
}
