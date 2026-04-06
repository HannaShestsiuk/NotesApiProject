package practice_app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import common.BasePage;

import static constants.Constants.DYNAMIC_TABLE_PAGE;

public class DynamicTablePage extends BasePage {
    public DynamicTablePage(Page page) {
        super(page);
    }

    @Override
    public String path() {
        return DYNAMIC_TABLE_PAGE;
    }

    private Locator chromeCpuCell() {
        String xpath = "//tr[td='Chrome']/td[count(//th[normalize-space()='CPU']/preceding-sibling::th)+1]";
        return page.locator(xpath);
    }

    @Step("Find Chrome CPU from table")
    public String chromeCpuFromTableText() {
        Locator cell = chromeCpuCell();
        cell.waitFor();
        return cell.innerText().trim();
    }

    @Step("Get Chrome CPU from label")
    public String chromeCpuFromLabelText() {
        Locator label = page.locator("#chrome-cpu");
        label.waitFor();
        return label.innerText().trim();
    }

    @Step("Compare Chrome CPU values")
    public boolean compareCpuValues() {
        String tableValue = chromeCpuFromTableText();
        String labelText = chromeCpuFromLabelText();
        return labelText.contains(tableValue);
    }
}
