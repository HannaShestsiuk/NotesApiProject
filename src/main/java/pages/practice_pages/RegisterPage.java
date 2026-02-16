package pages.practice_pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import helpers.VisibleLocator;
import io.qameta.allure.Step;
import pages.BasePage;
import records.practice_records.PracticeUiUser;

import static constants.Constants.LOGIN_PAGE;
import static constants.Constants.REGISTER_PAGE;

public class RegisterPage extends BasePage {
    public RegisterPage(Page page) {
        super(page);
    }

    @Override
    public String path() {
        return REGISTER_PAGE;
    }

    private Locator usernameInput() {
        return page.locator("#username");
    }

    private Locator passwordInput() {
        return page.locator("#password");
    }

    private Locator confirmPasswordInput() {
        return page.locator("#confirmPassword");
    }

    private Locator registerButton() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Register")
        );
    }

    public VisibleLocator flashMessage() {
        return should(page.locator("#flash-message b"));
    }


    public Locator errorMessage() {
        return page.locator("b:has-text('An error occurred during registration')");
    }

    @Step("Fill username input with {username}")
    public void fillUserName(String username){
        usernameInput().fill(username);
    }

    @Step("Fill password")
    public void fillPassword(String password){
        passwordInput().fill(password);
    }

    @Step("Fill confirmPassword")
    public void fillConfirmPassword(String confirmPassword){
        confirmPasswordInput().fill(confirmPassword);
    }

    @Step("Click 'Register' button")
    public LoginPage clickRegister(){
        registerButton().click();
        page.waitForURL("**" + LOGIN_PAGE);
        return new LoginPage(page);
    }

    @Step("Click 'Register' button (expecting validation error)")
    public void clickRegisterExpectingFailure() {
        registerButton().click();
    }

    public LoginPage fillRegisterForm(PracticeUiUser user){
        fillUserName(user.getUserName());
        fillPassword(user.getPassword());
        fillConfirmPassword(user.getConfirmPassword());
        return clickRegister();
    }

    public void fillRegisterFormExpectingFailure(PracticeUiUser user){
        fillUserName(user.getUserName());
        fillPassword(user.getPassword());
        fillConfirmPassword(user.getConfirmPassword());
        clickRegisterExpectingFailure();
    }
}
