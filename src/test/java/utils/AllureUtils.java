package utils;

import io.qameta.allure.Allure;
import io.restassured.response.Response;

public class AllureUtils {
    /**
     * Attaches HTTP response body to Allure report with status code as attachment title.
     *
     * @param response the HTTP response to attach to the Allure report
     */
    public static void attachResponseToAllure(Response response) {
        String body = response.getBody().asPrettyString(); // или response.asPrettyString()
        int statusCode = response.getStatusCode();
        String contentType = response.getHeader("Content-Type");
        if (contentType == null) contentType = "application/json";

        Allure.addAttachment("Response_log_status_" + statusCode + ".json", contentType, body);
    }
}
