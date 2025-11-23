package notesApi;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import requests.SimpleActions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;


public class HealthCheckTest {
    @Test
    public void healthCheckTest () {
        Response response = SimpleActions.healthCheck();

        assertAll("Health-check response validation",
            () -> assertThat(response.jsonPath().getBoolean("success"), is(true)),
            () -> assertThat(response.jsonPath().getInt("status"), is(200)),
            () -> assertThat(response.jsonPath().getString("message"), equalTo("Notes API is Running"))
        );
    }
}
