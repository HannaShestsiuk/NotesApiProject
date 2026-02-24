package ui_tests.practice;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.practice_pages.*;
import ui_tests.BaseTest;

public class WebInputsTest extends BaseTest {

    @DisplayName("[UI]. Registration page. Validate successful user registration")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Web Inputs Page' page.
    3. Fill in Inputs form.
    4. Click on Display Inputs button.
    5. Validate outputs match input.
    6. Clear Inputs form
    """)
    @Test
    void webInputTest() {

        HomePage home = new HomePage(page()).open();
        WebInputsPage webInputsPage = home.goToWebInputsPage();

        webInputsPage.fillForm();
        webInputsPage.clickDisplayInputs();
        webInputsPage.validateOutput();
        webInputsPage.clickClearInputs();
    }
}
