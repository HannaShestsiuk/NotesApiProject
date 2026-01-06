package api_tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import requests.SimpleActions;

import static constants.ApiConstants.BASE_SCHEMA;
import static constants.Messages.HEALTH_CHECK;
import static org.junit.jupiter.api.Assertions.*;
import static utils.TestUtils.assertResponseSchema;


public class HealthCheckTest {
    @Test
    public void healthCheckTest () {
        Response response = SimpleActions.healthCheck();

        assertResponseSchema(BASE_SCHEMA, response);

        assertAll("Health-check response validation",
            () -> assertTrue(response.jsonPath().getBoolean("success"),"Invalid success status."),
            () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
            () -> assertEquals(HEALTH_CHECK, response.jsonPath().getString("message"), "Invalid message.")
        );
    }
}
