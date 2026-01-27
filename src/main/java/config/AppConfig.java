package config;

public final class AppConfig {

    public static final String BASE_WEB_URL = "https://practice.expandtesting.com/notes/app/";

    private AppConfig() {}

    public static String baseUrl() {
        return System.getProperty("baseUrl", BASE_WEB_URL);
    }
}