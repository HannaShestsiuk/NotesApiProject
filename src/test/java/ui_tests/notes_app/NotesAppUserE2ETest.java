package ui_tests.notes_app;

import helpers.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.notes_app.*;
import pages.practice_pages.HomePage;
import records.User;
import ui_tests.BaseTest;

/**
 * End-to-End test for the complete User lifecycle within the Notes App:
 * Registration -> Login -> Home Page -> Profile Management -> Account Deletion.
 */
public class NotesAppUserE2ETest extends BaseTest {

    @Test
    @DisplayName("User Lifecycle: Register -> Update Profile -> Delete Account")
    void userAccountFlowTest() {

        // 1. Generate unique User data using the Record and Faker helper
        User testUser = new User(
                TestDataGenerator.randomUserName(),
                TestDataGenerator.randomEmail(),
                TestDataGenerator.randomPassword(8, 16)
        );

        // 2. Navigate from the main Practice Home to the Notes App Welcome Page
        HomePage home = new HomePage(page()).open();
        NotesAppWelcomePage welcomePage = home.goToNotesAppWelcomePage();
        welcomePage.welcomePageShouldBeOpened();

        // 3. Complete Registration and verify the success alert
        NotesAppRegisterPage registerPage = welcomePage.clickCreateAccount()
                .registerPageShouldBeOpened()
                .register(testUser.email(), testUser.userName(), testUser.password())
                .successMessageShouldBeDisplayed();

        // 4. Navigate to Login and authenticate with the new credentials
        NotesAppLoginPage loginPage = registerPage.clickLoginLink();
        NotesAppHomePage notesHomePage = loginPage.login(testUser.email(), testUser.password());

        // 5. Verify successful login to the Notes App Home/Dashboard
        notesHomePage.homePageShouldBeOpened();

        // 6. Navigate to Profile Settings and perform updates
        // Requirement: Verify that User ID and Email fields are read-only (disabled)
        NotesAppProfilePage profilePage = notesHomePage.goToProfile();
        profilePage.profilePageShouldBeOpened()
                .identityFieldsShouldBeDisabled()
                .updateProfile(
                        TestDataGenerator.randomUserName(),
                        TestDataGenerator.randomPhone(10),
                        TestDataGenerator.randomCompany()
                )
                .successMessageShouldBeDisplayed();

        // 7. Execute Account Deletion flow and verify final redirection to Login
        // Requirement: Modal confirmation must be visible before deletion
        profilePage.clickDeleteAccount()
                .deleteModalShouldBeVisible()
                .confirmDeletion()
                .loginPageShouldBeOpened()
                .accountDeletedMessageShouldBeDisplayed();
    }
}