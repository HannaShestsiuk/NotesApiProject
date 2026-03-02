package pages.practice_pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import pages.BasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Constants.WEB_INPUT_PAGE;
import static helpers.TestDataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Page Object for the Web Inputs practice page.
 * This page contains various HTML5 input types (Number, Text, Password, Date).
 * It provides functionality to populate these fields using random data generators,
 * display the entered values, and verify the output section matches the input.
 */

public class WebInputsPage extends BasePage {

    /**
     * Initializes the WebInputsPage with the Playwright driver.
     * @param page The Playwright Page instance.
     */
    public WebInputsPage(Page page){
        super(page);
    }

    protected String path() {
        return WEB_INPUT_PAGE;
    }

    // --- Locators: Buttons ---

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

    // --- Locators: Inputs ---

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

    // --- Locators: Output Display ---

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

    // --- Actions ---

    /**
     * Generates and fills a random number into the numeric input field.
     */
    @Step("Fill Number input")
    public void fillNumber(){
        numberInput().fill(randomNumber());
    }

    /**
     * Generates and fills a random string into the text input field.
     */
    @Step("Fill Text input")
    public void fillText(){
        textInput().fill(randomString());
    }

    /**
     * Generates and fills a random password (with custom length 6-12) into the password field.
     */
    @Step("Fill Password input")
    public void fillPassword(){
        passwordInput().fill(randomPassword(6, 12));
    }

    /**
     * Generates and fills a random date into the date input field.
     */
    @Step("Fill Date input")
    public void fillDate(){
        dateInput().fill(randomDate());
    }

    /**
     * Clicks the 'Display Inputs' button to render the input values in the output section.
     */
    @Step("Click on 'Display Inputs' button")
    public void clickDisplayInputs() {
        displayInputsButton().click();
    }

    /**
     * Convenience method to populate all input fields on the page in one action.
     */
    public void fillForm() {
        fillNumber();
        fillText();
        fillPassword();
        fillDate();
    }

    // --- Data Retrieval ---

    /** @return The trimmed text currently displayed in the number output field. */
    public String getOutputNumber() {
        return numberOutput().innerText().trim();
    }

    /** @return The trimmed text currently displayed in the text output field. */
    public String getOutputText() {
        return textOutput().innerText().trim();
    }

    /** @return The trimmed text currently displayed in the password output field. */
    public String getOutputPassword() {
        return passwordOutput().innerText().trim();
    }

    /** @return The trimmed text currently displayed in the date output field. */
    public String getOutputDate() {
        return dateOutput().innerText().trim();
    }

    // --- Assertions & Validations ---

    /**
     * Compares the current value in the Number input field with its corresponding output display.
     * @return true if the input and output values match.
     */
    @Step("Validate that the Number output value matches the input value")
    public boolean isNumberOutputsEqualsInput() {
        return numberInput().inputValue().equals(getOutputNumber());
    }

    /**
     * Compares the current value in the Text input field with its corresponding output display.
     * @return true if the input and output values match.
     */
    @Step("Validate that the Text output value matches the input value")
    public boolean isTextOutputsEqualsInput() {
        return textInput().inputValue().equals(getOutputText());
    }

    /**
     * Compares the current value in the Password input field with its corresponding output display.
     * @return true if the input and output values match.
     */
    @Step("Validate that the Password output value matches the input value")
    public boolean isPasswordOutputsEqualsInput() {
        return passwordInput().inputValue().equals(getOutputPassword());
    }

    /**
     * Compares the current value in the Date input field with its corresponding output display.
     * @return true if the input and output values match.
     */
    @Step("Validate that the Date output value matches the input value")
    public boolean isDateOutputsEqualsInput() {
        return dateInput().inputValue().equals(getOutputDate());
    }

    /**
     * Performs a full validation of all output fields against their respective inputs using Playwright's
     * web-first assertions. This method will automatically retry if the output is not immediately available.
     * * @return This page instance for method chaining.
     */
    public WebInputsPage validateOutput() {
        assertThat(numberOutput()).hasText(numberInput().inputValue());
        assertThat(textOutput()).hasText(textInput().inputValue());
        assertThat(passwordOutput()).hasText(passwordInput().inputValue());
        assertThat(dateOutput()).hasText(dateInput().inputValue());
        return this;
    }

    /**
     * Clicks the 'Clear Inputs' button to reset all fields on the page.
     */
    @Step("Click on 'Clear Inputs' button")
    public void clickClearInputs() {
        clearInputsButton().click();
    }

    /**
     * Validates that all input fields have been successfully cleared.
     * @return This page instance.
     */
    @Step("Verify all input fields are empty")
    public WebInputsPage validateInputsAreEmpty() {
        assertThat(numberInput()).isEmpty();
        assertThat(textInput()).isEmpty();
        assertThat(passwordInput()).isEmpty();
        assertThat(dateInput()).isEmpty();
        return this;
    }
}
