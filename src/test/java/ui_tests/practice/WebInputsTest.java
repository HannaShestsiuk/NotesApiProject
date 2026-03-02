package ui_tests.practice;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.practice_pages.*;
import ui_tests.BaseTest;

public class WebInputsTest extends BaseTest {

    @DisplayName("[UI]. Web Inputs page. Verify input data display and reset functionality")
    @Description("""
    1. Open the Home Page (https://practice.expandtesting.com/).
    2. Navigate to the 'Web Inputs' page.
    3. Fill the form with random Number, Text, Password, and Date.
    4. Click the 'Display Inputs' button.
    5. Validate that all output values match the entered input values.
    6. Click the 'Clear Inputs' button.
    7. Verify that all input fields are empty.
    """)
    @Test
    void webInputTest() {

        HomePage home = new HomePage(page()).open();
        // Navigation and Data Entry
        WebInputsPage webInputsPage = home.goToWebInputsPage();
        webInputsPage.fillForm();
        webInputsPage.clickDisplayInputs();

        // Validation of Output
        webInputsPage.validateOutput();

        // Reset and Final Assertion
        webInputsPage.clickClearInputs();
        webInputsPage.validateInputsAreEmpty();
    }
}
