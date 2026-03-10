package ui_tests.notes_app;

import helpers.TestDataGenerator;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.notes_app.*;
import pages.practice_pages.HomePage;
import records.User;
import records.UserProfile;
import ui_tests.BaseTest;

/**
 * End-to-End test for the complete User lifecycle within the Notes App:
 * Registration -> Login -> Home Page -> Profile Management -> Account Deletion.
 */
public class NotesAppUserE2ETest extends BaseTest {

    @Test
    @DisplayName("[UI]. Notes App. User Lifecycle: Register -> Login -> Update Profile -> Delete Account")
    @Description("""
    1. Navigate to the practice home page and open the 'Notes App' landing page.
    2. Verify that the Notes App Welcome page is successfully loaded.
    3. Navigate to the Registration page and create a new account using unique dynamic data.
    4. Confirm successful registration via the visibility of the success alert message.
    5. Click the 'Login' link and authenticate using the newly created credentials.
    6. Verify redirection to the Notes App Home/Dashboard.
    7. Navigate to the User Profile settings.
    8. Validate that security-sensitive fields (User ID and Email) are read-only/disabled.
    9. Update profile information (Name, Phone, Company) and verify the update success alert and updated profile fields.
    10. Initiate account deletion and verify the confirmation modal is displayed.
    11. Confirm deletion and verify automatic redirection back to the Login page.
    12. Assert that the 'Account Deleted' danger alert is correctly displayed on the Login page.
    """)
    void userCreateUpdateAndDeleteAccountTest() {

        User testUser = new User(
                TestDataGenerator.randomUserName(),
                TestDataGenerator.randomEmail(),
                TestDataGenerator.randomPassword(8, 16)
        );

        HomePage home = new HomePage(page()).open();
        NotesAppWelcomePage welcomePage = home.goToNotesAppWelcomePage();
        welcomePage.welcomePageShouldBeOpened();

        NotesAppRegisterPage registerPage = welcomePage.clickCreateAccount()
                .registerPageShouldBeOpened()
                .register(testUser)
                .successMessageShouldBeDisplayed();

        NotesAppLoginPage loginPage = registerPage.clickLoginLink();
        NotesAppHomePage notesHomePage = loginPage.login(testUser);

        notesHomePage.homePageShouldBeOpened();

        UserProfile updatedProfile = new UserProfile(
                TestDataGenerator.randomUserName(),
                TestDataGenerator.randomPhone(10),
                TestDataGenerator.randomCompany()
        );

        NotesAppProfilePage profilePage = notesHomePage.goToProfile();
        profilePage.profilePageShouldBeOpened()
                .identityFieldsShouldBeDisabled()
                .updateProfile(updatedProfile)
                .successMessageShouldBeDisplayed()
                .profileFieldsShouldMatch(updatedProfile);

        profilePage.clickDeleteAccount()
                .deleteModalShouldBeVisible()
                .confirmDeletion()
                .loginPageShouldBeOpened()
                .accountDeletedMessageShouldBeDisplayed();
    }
}