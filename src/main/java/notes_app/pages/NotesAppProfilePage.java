package notes_app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import common.BasePage;
import records.UserProfile;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Constants.NOTES_PROFILE_PAGE;

/**
 * Page Object for the Notes App Profile Settings page.
 * Handles viewing and updating account details, and account deletion.
 */
public class NotesAppProfilePage extends BasePage {

    public NotesAppProfilePage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return NOTES_PROFILE_PAGE;
    }

    // --- Locators ---
    private Locator profileHeading() {
        return page.locator("h1");
    }

    private Locator userIdInput() {
        return page.getByTestId("user-id");
    }

    private Locator userEmailInput() {
        return page.getByTestId("user-email");
    }

    private Locator userNameInput() {
        return page.getByTestId("user-name");
    }

    private Locator userPhoneInput() {
        return page.getByTestId("user-phone");
    }

    private Locator userCompanyInput() {
        return page.getByTestId("user-company");
    }

    private Locator updateProfileButton() {
        return page.getByTestId("update-profile");
    }

    private Locator deleteAccountButton() {
        return page.getByTestId("delete-account");
    }

    // Delete Modal Locators

    private Locator deleteModal() {
        return page.getByTestId("note-delete-dialog");
    }

    private Locator confirmDeleteButton() {
        return page.getByTestId("note-delete-confirm");
    }

    private Locator cancelDeleteButton() {
        return page.getByTestId("note-delete-cancel-2");
    }

    // Alert Locators
    private Locator successAlert() { return page.getByTestId("alert-message"); }

    // --- Assertions & Validations ---

    @Step("Verify that Profile Settings page is loaded")
    public NotesAppProfilePage profilePageShouldBeOpened() {
        assertThat(page).hasURL(Pattern.compile(".*" + NOTES_PROFILE_PAGE));
        assertThat(profileHeading()).hasText("Profile settings");
        return this;
    }

    @Step("Verify that Delete Account modal is visible")
    public NotesAppProfilePage deleteModalShouldBeVisible() {
        assertThat(deleteModal()).isVisible();
        assertThat(deleteModal().locator(".modal-title"))
                .hasText("Do you really want to delete your account?");
        return this;
    }

    /**
     * Verifies that the User ID and Email fields are read-only/disabled.
     */
    @Step("Verify that User ID and Email fields are disabled")
    public NotesAppProfilePage identityFieldsShouldBeDisabled() {
        assertThat(userIdInput()).isDisabled();
        assertThat(userEmailInput()).isDisabled();
        return this;
    }

    @Step("Verify success message is displayed after update")
    public NotesAppProfilePage successMessageShouldBeDisplayed() {
        assertThat(successAlert()).isVisible();
        assertThat(successAlert()).hasText("Profile updated successful");
        return this;
    }

    // --- Actions ---

    /**
     * Updates the user profile information.
     * @param profile The {@link UserProfile} record containing name, phone, and company.
     * @return This page instance.
     */
    @Step("Update profile with: {profile.name}")
    public NotesAppProfilePage updateProfile(UserProfile profile) {
        userNameInput().fill(profile.name());
        userPhoneInput().fill(profile.phone());
        userCompanyInput().fill(profile.company());
        updateProfileButton().click(); // Assuming you have a save button locator
        return this;
    }

    /**
     * Asserts that the profile fields match the provided UserProfile data.
     * @param expectedProfile The data we expect to see in the fields.
     * @return This page instance.
     */
    @Step("Verify profile fields match the updated profile data")
    public NotesAppProfilePage profileFieldsShouldMatch(UserProfile expectedProfile) {
        assertThat(userNameInput()).hasValue(expectedProfile.name());
        assertThat(userPhoneInput()).hasValue(expectedProfile.phone());
        assertThat(userCompanyInput()).hasValue(expectedProfile.company());
        return this;
    }

    /**
     * Triggers the delete account flow by clicking the main delete button.
     */
    @Step("Click on 'Delete Account' button")
    public NotesAppProfilePage clickDeleteAccount() {
        deleteAccountButton().click();
        return this;
    }

    /**
     * Confirms account deletion in the modal.
     * @return NotesAppWelcomePage as the user is logged out and account removed.
     */
    @Step("Confirm account deletion in modal")
    public NotesAppLoginPage confirmDeletion() {
        confirmDeleteButton().click();
        // The app redirects to the Login page with the deletion notice
        return new NotesAppLoginPage(page);
    }

    /**
     * Cancels the deletion process.
     */
    @Step("Cancel account deletion")
    public NotesAppProfilePage cancelDeletion() {
        cancelDeleteButton().click();
        assertThat(deleteModal()).isHidden();
        return this;
    }
}
