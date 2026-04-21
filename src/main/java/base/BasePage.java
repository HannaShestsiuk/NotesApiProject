package base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import config.AppConfig;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

public abstract class BasePage {
    protected final Page page;
    public final String ALERT = "//div[contains(@class,'alert') and contains(.,'%s')]";
    public BasePage(Page page) {
        this.page = page;
    }

    // Every page defines its own URL path
    protected abstract String path();

    public <T extends BasePage> T open() {
        page.navigate(AppConfig.baseUrl() + path());
        return (T) this;
    }

    /*========BASE UI-ACTIONS=========*/
    public void click(String locator){
        page.locator(locator).click();
    }

    public void linkClick(String locator){
        page.locator(locator).click();
    }

    public void fill(String locator, String value){
        page.locator(locator).fill(value);
    }

    public void waitVisible(String locator){
        page.locator(locator)
                .waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    public void isAlertVisible(String text) {
    }
}
