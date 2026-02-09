package pages.notesApp;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.BasePage;
import records.UiUser;

public class NotesAppRegistrationPage extends BasePage {

    public NotesAppRegistrationPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return "/register"; // or whatever your app uses
    }

    private Locator emailAddressField() {
        return page.locator("[data-testid='register-email']");
    }

    private Locator nameField() {
        return page.locator("[data-testid='register-userName']");
    }

    private Locator passwordField() {
        return page.locator("[data-testid='register-password']");
    }

    private Locator confirmPasswordField() {
        return page.locator("[data-testid='register-confirm-password']");
    }

    private Locator registerButton() {
        return page.locator("[data-testid='register-submit']");
    }

    public NotesAppRegistrationPage fillForm(UiUser user) {
        emailAddressField().fill(user.email());
        nameField().fill(user.name());
        passwordField().fill(user.password());
        confirmPasswordField().fill(user.confirmPassword());
        return this;
    }

    public void register() {
        registerButton().click();
    }
}

