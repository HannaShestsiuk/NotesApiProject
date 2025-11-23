package notesApi;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import requests.SimpleActions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HealthCheckTest {
    @Test
    public void healthCheckTest () {
        Response response = SimpleActions.healthCheck();

        assertAll("Health-check response validation",
            () -> assertEquals(response.jsonPath().getBoolean("success"),true),
            () -> assertEquals(response.jsonPath().getInt("status"),200),
            () -> assertEquals(response.jsonPath().getString("message"), "Notes API is Running")
        );
    }
}
