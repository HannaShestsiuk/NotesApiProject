package pages.PracticePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.BasePage;
import records.UiUser;

public class RegisterPage extends BasePage {
    public RegisterPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return "/register";
    }

    private Locator usernameField() {
        return page.locator("#username");
    }

    private Locator passwordField() {
        return page.locator("#password");
    }

    private Locator confirmPasswordField() {
        return page.locator("#confirmPassword");
    }

    private Locator registerButton() {
        return page.locator("button:has-text('Register')");
    }

    private Locator successMessage() {
        return page.locator("b:has-text('Successfully registered')");
    }

    private Locator errorMessage() {
        return page.locator("b:has-text('An error occurred during registration')");
    }

    @Step("Fill registration form for user: {user.name()}")
    public RegisterPage fillForm(UiUser user) {
        usernameField().fill(user.name());
        passwordField().fill(user.password());
        confirmPasswordField().fill(user.confirmPassword());
        return this;
    }

    public void register() {
        registerButton().click();
    }
}
