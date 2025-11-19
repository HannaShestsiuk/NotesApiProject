package healthCheck;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import notesApi.ApiConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;


public class HealthCheckTest {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ApiConstants.BASE_URI;
    }

    @Test
    public void healthCheckTest () {
        //RestAssured.baseURI = "https://practice.expandtesting.com/notes/api";
        Response response = RestAssured
                .given()
                .when()
                .get("/health-check");
//                .then()
//                .extract()
//                .response();

        assertAll("Health-check response validation",
            () -> assertThat(response.jsonPath().getBoolean("success"), is(true)),
            () -> assertThat(response.jsonPath().getInt("status"), is(200)),
            () -> assertThat(response.jsonPath().getString("message"), equalTo("Notes API is Running"))
        );
    }

    @Test
    public void healthCheckTestII() {
        //RestAssured.baseURI = "https://practice.expandtesting.com/notes/api";

        // Send a GET request to the API
        Response response = RestAssured.get("/health-check");

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
        response.then().assertThat().body("status", equalTo(200));
        response.then().assertThat().body("message", equalTo("Notes API is Running"));
    }

    @Test
    public void healthCheckTestIII () {
        //RestAssured.baseURI = "https://practice.expandtesting.com/notes/api";
        RestAssured
                .given()
                .when()
                .get("/health-check")
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("status", equalTo(200))
                .body("message", equalTo("Notes API is Running"))
                .extract()
                .response();
    }
}
