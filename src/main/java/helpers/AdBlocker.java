package helpers;

import com.microsoft.playwright.Page;

public class AdBlocker {

    public static void blockInterstitialAds(Page page) {
        // блокировка редиректов с хешами google_vignette и adtech_redirect
        page.route("**/*", route -> {
            String url = route.request().url();
            if (url.contains("google_vignette") || url.contains("adtech_redirect")) {
                System.out.println("🚫 Blocked interstitial ad: " + url);
                route.abort();
            } else {
                route.resume();
            }
        });

        // если реклама открывается через hashchange (после загрузки)
        page.onFrameNavigated(frame -> {
            String url = frame.url();
            if (url.contains("google_vignette") || url.contains("adtech_redirect")) {
                System.out.println("🚫 Prevented redirect ad: " + url);
                page.evaluate("window.location.href = window.location.href.split('#')[0];");
            }
        });
    }

    public static void killInterstitialAds(Page page) {
        page.addInitScript("""
        const observer = new MutationObserver(() => {
          const ad = document.querySelector("#google_vignette, #adtech_redirect");
          if (ad) ad.remove();
        });
        observer.observe(document.documentElement, { childList: true, subtree: true });
    """);
    }
}
