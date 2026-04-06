package practice_app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import common.BasePage;

import static constants.Constants.RADIO_BUTTONS_PAGE;


/**
 * Page Object for the Radio Buttons practice page.
 * Provides methods to select and verify states of color and sport radio buttons.
 */
public class RadioButtonsPage extends BasePage {

    public RadioButtonsPage(Page page){
        super(page);
    }
    @Override
    protected String path() {
        return RADIO_BUTTONS_PAGE;
    }

    private Locator getRadioButton(String id) {
        return page.locator("#" + id);
    }

    /**
     * Selects a radio button by its ID.
     *
     * @param id The ID of the radio button (e.g., "blue", "football").
     */
    @Step("Select radio button with ID: {id}")
    public void selectRadioButton(String id) {
        getRadioButton(id).check();
    }

    /**
     * Checks if a specific radio button is selected.
     * @param id The ID of the radio button.
     * @return true if checked.
     */
    public boolean isRadioButtonSelected(String id) {
        return getRadioButton(id).isChecked();
    }

    public boolean isRadioButtonUnselected(String id) {
        return !getRadioButton(id).isChecked();
    }

    /**
     * Checks if a specific radio button is disabled.
     * @param id The ID of the radio button.
     * @return true if disabled.
     */
    public boolean isRadioButtonDisabled(String id) {
        return !getRadioButton(id).isEnabled();
    }
}
