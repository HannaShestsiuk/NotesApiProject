package testdata;

import records.User;
import records.practice_records.PracticeUiUser;

public class TestUsers {
    public static PracticeUiUser practiceUser(){
        return new PracticeUiUser(
                "practice",
                "SuperSecretPassword!",
                "SuperSecretPassword!"
        );
    }
}
