package ui_tests.practice;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.practice_pages.HomePage;
import pages.practice_pages.RadioButtonsPage;
import ui_tests.BaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RadioButtonsTest extends BaseTest {
    @Test
    @DisplayName("[UI]. Radio Buttons page. Verify Radio Button selection and states")
    @Description("""
        1. Open the Home Page (https://practice.expandtesting.com/).
        2. Navigate to the 'Radio Buttons' page.
        3. Verify that the 'Blue' radio button is selected by default in the Color section.
        4. Select the 'Red' radio button and verify it is selected while 'Blue' becomes unselected.
        5. Verify that the 'Green' radio button is disabled and cannot be selected.
        6. Verify that the 'Tennis' radio button is selected by default in the Sport section.
        7. Select the 'Football' radio button and verify it is selected while 'Tennis' becomes unselected.
        """)
    void testRadioButtons() {
        HomePage home = new HomePage(page()).open();
        RadioButtonsPage radioButtonsPage = home.goToRadioButtonsPage();

        // Check radio buttons selection under Color section
        assertTrue(radioButtonsPage.isRadioButtonSelected("blue"));
        radioButtonsPage.selectRadioButton("red");
        assertTrue(radioButtonsPage.isRadioButtonSelected("red"));
        assertTrue(radioButtonsPage.isRadioButtonUnselected("blue"));

        // Check radio button is disabled under Color section
        assertTrue(radioButtonsPage.isRadioButtonDisabled("green"));

        // Check radio buttons for Sport section
        assertTrue(radioButtonsPage.isRadioButtonSelected("tennis"));
        radioButtonsPage.selectRadioButton("football");
        assertTrue(radioButtonsPage.isRadioButtonSelected("football"));
        assertTrue(radioButtonsPage.isRadioButtonUnselected("tennis"));
    }
}
