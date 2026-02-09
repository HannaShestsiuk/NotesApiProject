package pages.practicePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import helpers.VisibleLocator;
import io.qameta.allure.Step;
import pages.BasePage;
import records.practiceRecords.PracticeUiUser;

import static constants.Constants.LOGIN_PAGE;
import static constants.Constants.SECURE_PAGE;
import static constants.Messages.*;

public class LoginPage extends BasePage {
    public LoginPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return LOGIN_PAGE;
    }

    private Locator usernameInput() {
        return page.locator("#username");
    }

    private Locator passwordInput() {
        return page.locator("#password");
    }

    private Locator loginButton() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Login")
        );
    }

    public VisibleLocator flashMessage() {
        return should(page.locator("#flash-message b"));
    }

    public Locator successRegisteredMessage = page.getByText(SUCCESSFUL_REGISTRATION);

    public Locator invalidUserNameMessage = page.getByText(LOGIN_INVALID_USERNAME);

    public Locator invalidPasswordMessage = page.getByText(LOGIN_INVALID_PASSWORD);

    public Locator logoutMessage = page.getByText(LOGOUT_MESSAGE);

    @Step("Fill userName input with {username}")
    public void fillUserName(String username){
        usernameInput().fill(username);
    }

    @Step("Fill password")
    public void fillPassword(String password){
        passwordInput().fill(password);
    }

    @Step("Click 'Login' button")
    public SecurePage clickLogin(){
        loginButton().click();
        page.waitForURL("**" + SECURE_PAGE);
        return new SecurePage(page);
    }

    @Step("Click 'Login' button (expecting validation error)")
    public LoginPage clickLoginExpectingFailure() {
        loginButton().click();
        return this;
    }

    public SecurePage fillLoginForm(PracticeUiUser user){
        fillUserName(user.getUserName());
        fillPassword(user.getPassword());
        return clickLogin();
    }

    public LoginPage fillLoginFormExpectingFailure(PracticeUiUser user){
        fillUserName(user.getUserName());
        fillPassword(user.getPassword());
        return clickLoginExpectingFailure();
    }

    @Step("Assert that Login Page is opened")
    public void loginPageShouldBeOpened() {
        page.waitForURL("**" + LOGIN_PAGE);

        page.getByRole(AriaRole.HEADING,
                        new Page.GetByRoleOptions().setName("Test Login Page"))
                .waitFor();
    }
}
