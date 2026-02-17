package pages.practice_pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import pages.BasePage;

import static constants.Constants.FORGOT_PASSWORD_PAGE;
import static constants.Messages.PASSWORD_RESET_SENT;

public class ForgotPasswordPage extends BasePage {

    public ForgotPasswordPage(Page page) {
        super(page);
    }

    @Override
    protected String path() {
        return FORGOT_PASSWORD_PAGE;
    }

    @Override
    public void isAlertVisible(String text) {
        page.locator("div.invalid-feedback")
                .getByText(text)
                .waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );
    }

    private Locator emailInput() {
        return page.locator("#email");
    }

    private Locator retrievePasswordButton() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Retrieve password")
        );
    }

    private Locator emailSentMessage() {
        return page.getByRole(AriaRole.ALERT)
                .filter(new Locator.FilterOptions().setHasText(PASSWORD_RESET_SENT)
        );
    }

    @Step("Wait for email sent confirmation message")
    public void waitForEmailSentMessage() {
        emailSentMessage().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Fill Email")
    public void fillEmail(String email){
        emailInput().fill(email);
    }

    @Step("Click 'Retrieve password' button")
    public void clickRetrievePassword(){
        retrievePasswordButton().click();
        page.waitForURL("**" + FORGOT_PASSWORD_PAGE);
    }

    public void fillForgotPasswordForm(String email){
        fillEmail(email);
        clickRetrievePassword();
    }
}
