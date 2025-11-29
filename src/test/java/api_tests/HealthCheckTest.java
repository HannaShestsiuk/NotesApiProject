package api_tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import requests.SimpleActions;

import static org.junit.jupiter.api.Assertions.*;


public class HealthCheckTest {
    @Test
    public void healthCheckTest () {
        Response response = SimpleActions.healthCheck();

        assertAll("Health-check response validation",
            () -> assertTrue(response.jsonPath().getBoolean("success"),"Invalid success status."),
            () -> assertEquals(200, response.jsonPath().getInt("status"), "Invalid Status Code."),
            () -> assertEquals("Notes API is Running", response.jsonPath().getString("message"), "Invalid message.")
        );
    }
}
