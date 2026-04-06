package notes_app;

import com.microsoft.playwright.Page;

public class BaseComponent {
    protected final Page page;

    protected BaseComponent(Page page) {
        this.page = page;
    }
}
