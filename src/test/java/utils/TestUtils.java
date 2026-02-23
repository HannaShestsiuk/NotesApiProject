package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {

    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);
    /**
     * Validates the schema of a response against a given JSON schema file.
     *
     * @param pathToSchema The relative path to the schema file.
     * @param response     The API response.
     * @return true if schema validation passed, false otherwise.
     */
    @Step("Validation of response schema")
    public static boolean assertResponseSchema(String pathToSchema, Response response) {
        boolean result = false;

        Allure.addAttachment("Response Body", "application/json", response.getBody().asPrettyString());

        try {
            if (response.getBody().asString().equals("[]")) {
                logger.info("Response for - "
                        + pathToSchema
                        + "has an empty array. Skipping schema validation.");
            } else {
                response
                        .then()
                        .assertThat()
                        .body(JsonSchemaValidator
                                .matchesJsonSchemaInClasspath(pathToSchema));
                logger.info("Schema validation - " + pathToSchema + " - PASSED");
                result = true;
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!result) {
                logger.info("Schema validation - " + pathToSchema + " - FAILED");
            }
        }
    }
}
