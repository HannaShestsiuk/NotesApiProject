package pages.practice_pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import pages.BasePage;

import static constants.Constants.WEB_INPUT_PAGE;
import static helpers.TestDataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Page Object for the Web Inputs practice page.
 * Handles interaction with various input types and verification of displayed results.
 */

public class WebInputsPage extends BasePage {
    public WebInputsPage(Page page){
        super(page);
    }

    protected String path() {
        return WEB_INPUT_PAGE;
    }

    private Locator displayInputsButton() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Display Inputs")
        );
    }

    private Locator clearInputsButton() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Clear Inputs")
        );
    }

    private Locator numberInput() {
        return page.locator("#input-number");
    }

    private Locator textInput() {
        return page.locator("#input-text");
    }

    private Locator passwordInput() {
        return page.locator("#input-password");
    }

    private Locator dateInput() {
        return page.locator("#input-date");
    }

    private Locator numberOutput() {
        return page.locator("#output-number");
    }

    private Locator textOutput() {
        return page.locator("#output-text");
    }

    private Locator passwordOutput() {
        return page.locator("#output-password");
    }

    private Locator dateOutput() {
        return page.locator("#output-date");
    }

    @Step("Fill Number input")
    public void fillNumber(){
        numberInput().fill(randomNumber());
    }

    @Step("Fill Text input")
    public void fillText(){
        textInput().fill(randomString());
    }

    @Step("Fill Password input")
    public void fillPassword(){
        passwordInput().fill(randomPassword(6, 12));
    }

    @Step("Fill Date input")
    public void fillDate(){
        dateInput().fill(randomDate());
    }

    @Step("Click on 'Display Inputs' button")
    public void clickDisplayInputs() {
        displayInputsButton().click();
    }

    public WebInputsPage fillForm() {
        fillNumber();
        fillText();
        fillPassword();
        fillDate();
        return this;
    }

    public String getOutputNumber() {
        return numberOutput().innerText().trim();
    }

    public String getOutputText() {
        return textOutput().innerText().trim();
    }

    public String getOutputPassword() {
        return passwordOutput().innerText().trim();
    }

    public String getOutputDate() {
        return dateOutput().innerText().trim();
    }

    @Step("Validate that the Number output value matches the input value")
    public boolean isNumberOutputsEqualsInput() {
        return numberInput().inputValue().equals(getOutputNumber());
    }

    @Step("Validate that the Text output value matches the input value")
    public boolean isTextOutputsEqualsInput() {
        return textInput().inputValue().equals(getOutputText());
    }

    @Step("Validate that the Password output value matches the input value")
    public boolean isPasswordOutputsEqualsInput() {
        return passwordInput().inputValue().equals(getOutputPassword());
    }

    @Step("Validate that the Date output value matches the input value")
    public boolean isDateOutputsEqualsInput() {
        return dateInput().inputValue().equals(getOutputDate());
    }

    public WebInputsPage validateOutput() {
        assertTrue(isNumberOutputsEqualsInput(), "Number mismatch!");
        assertTrue(isTextOutputsEqualsInput(), "Text mismatch!");
        assertTrue(isPasswordOutputsEqualsInput(), "Password mismatch!");
        assertTrue(isDateOutputsEqualsInput(), "Date mismatch!");
        return this;
    }

    @Step("Click on 'Clear Inputs' button")
    public void clickClearInputs() {
        clearInputsButton().click();
    }
}
