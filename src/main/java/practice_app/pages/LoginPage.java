package practice_app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import pages.BasePage;
import records.practice_records.PracticeUiUser;

import static constants.Constants.LOGIN_PAGE;
import static constants.Constants.SECURE_PAGE;

public class LoginPage extends BasePage {
    public LoginPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return LOGIN_PAGE;
    }

    @Override
    public void isAlertVisible(String text) {
        page.locator(String.format(ALERT, text)).waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );
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
    public void clickLoginExpectingFailure() {
        loginButton().click();
    }

    public SecurePage fillLoginForm(PracticeUiUser user){
        fillUserName(user.getUserName());
        fillPassword(user.getPassword());
        return clickLogin();
    }

    public void fillLoginFormExpectingFailure(PracticeUiUser user){
        fillUserName(user.getUserName());
        fillPassword(user.getPassword());
        clickLoginExpectingFailure();
    }

    @Step("Assert that Login Page is opened")
    public void loginPageShouldBeOpened() {
        page.waitForURL("**" + LOGIN_PAGE);

        page.getByRole(AriaRole.HEADING,
                        new Page.GetByRoleOptions().setName("Test Login Page"))
                .waitFor();
    }
}
