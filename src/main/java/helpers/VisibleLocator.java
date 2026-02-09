package helpers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;

public class VisibleLocator {

    private final Locator locator;

    public VisibleLocator(Locator locator) {
        this.locator = locator;
    }

    public void shouldBeVisible() {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));
    }

    public Locator get() {
        return locator;
    }
}
