package ui_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import pages.NotesApp.NotesAppHomePage;
import pages.NotesApp.NotesAppRegistrationPage;
import records.UiUser;
import records.User;

import java.util.stream.Stream;

import static helpers.TestDataGenerator.*;

public class RegistrationTests extends BaseTest {
    private static Stream<Arguments> validTestDataProvider(){
        return Stream.of(
                Arguments.of("Valid User Profile",
                        new User(
                                randomUserName(),
                                randomEmail(),
                                randomPassword(8, 10)
                        ))
        );
    }

    @Test
    public void userRegistrationTest(){
        String password = randomPassword(8, 10);
        UiUser user = new UiUser(randomEmail(), randomUserName(), password, password);

        NotesAppHomePage homePage = new NotesAppHomePage(page()).open();

        NotesAppRegistrationPage registrationPage = homePage.clickCreateAccountButton();

        registrationPage.fillForm(user).register();

    }
}
