package config;

import static constants.Constants.BASE_URL;

public final class AppConfig {

    private AppConfig() {}

    public static String baseUrl() {
        return System.getProperty("baseUrl", BASE_URL);
    }
}