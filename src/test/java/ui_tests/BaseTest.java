package ui_tests;

import com.microsoft.playwright.*;
import helpers.AdBlocker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import records.PracticeRecords.PracticeUiUser;


import java.nio.file.Paths;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;
import static constants.Constants.BASE_URL;
import static constants.Locators.HOME_PAGE_TEXT;


public class BaseTest {
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();
    private final ThreadLocal<Playwright> playwright = new ThreadLocal<>();

    @BeforeEach
    public void beforeEach() {
        boolean isHeadless = Boolean.parseBoolean(
                System.getenv().getOrDefault("HEADLESS", "true")
        );

        Playwright pw = Playwright.create();
        Browser br = pw
                .chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setHeadless(isHeadless));
        System.out.println(br.version());
        BrowserContext ctx = br.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setRecordVideoDir(Paths.get("./target/video")));

        ctx.route("**/*", route -> {
            String url = route.request().url();

            if(url.contains("doubleclick") ||
                    url.contains("googlesyndication") ||
                    url.contains("adservice") ||
                    url.contains("googleads") ||
                    url.contains("g.doubleclick.net")) {
                System.out.println("Preventing AD request: " + url);
                route.abort();
            } else {
                route.resume();
            }
        });

        Page pg = ctx.newPage();
        playwright.set(pw);
        browser.set(br);
        context.set(ctx);
        page.set(pg);
        AdBlocker.killInterstitialAds(pg);

        pg.navigate(BASE_URL);
        pg.locator(HOME_PAGE_TEXT)
                .waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    // Global user credentials
    protected static final PracticeUiUser globalUser =
            new PracticeUiUser("practice", "SuperSecretPassword!", "SuperSecretPassword!");


    @AfterEach
    public void afterEach() {
        context.get().close();
        browser.get().close();
        playwright.get().close();

        page.remove();
        context.remove();
        browser.remove();
        playwright.remove();
    }

    protected Page page() {
        return page.get();
    }
}
