package notesApi.users;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import notesApi.ApiConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserRegistrationTest {
    private static String registeredEmail;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ApiConstants.BASE_URI;
    }

    @Test
    public void registerUserWithValidData() {
        String name = "Test User";
        String email = System.currentTimeMillis() + "@mail.com";
        String password = "Strong123!";

        Response response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8") // charset=utf-8 !!!
                .formParam("name", name)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/users/register")
                .then()
                .extract()
                .response();

        registeredEmail = email;

        assertAll("Health-check response validation",
                () -> assertThat(response.jsonPath().getString("message"), is("User account created successfully")),
                () -> assertThat(response.jsonPath().getInt("status"), is(201)),
                () -> assertThat(response.jsonPath().getString("data.id"), notNullValue()), // how to check it is not empty??
                () -> assertThat(response.jsonPath().getString("data.name"), is(name)),
                () -> assertThat(response.jsonPath().getString("data.email"), is(email))
        );
    }

    @Test
    public void registerUserWithExistingEmail_ShouldBeFailed() {
        String name = "User " + System.currentTimeMillis();
        String email = registeredEmail;
        String password = "Strong123!";

        Response response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8") // charset=utf-8 !!!
                .formParam("name", name)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/users/register")
                .then()
                .extract()
                .response();

        assertAll("Health-check response validation",
                () -> assertThat(response.jsonPath().getString("message"), is("An account already exists with the same email address")),
                () -> assertThat(response.jsonPath().getInt("status"), is(409)),
                () -> assertThat(response.jsonPath().getBoolean("success"), is(false))
        );
    }

    @Test
    public void registerUserWithEmptyName_ShouldBeFailed() {
        String name = null;
        String email = System.currentTimeMillis() + "@mail.com";
        String password = "Strong123!";

        Response response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8") // charset=utf-8 !!!
                .formParam("name", name)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/users/register")
                .then()
                .extract()
                .response();

        assertAll("Health-check response validation",
                () -> assertThat(response.jsonPath().getString("message"), is("User name must be between 4 and 30 characters")),
                () -> assertThat(response.jsonPath().getInt("status"), is(400)),
                () -> assertThat(response.jsonPath().getBoolean("success"), is(false))
        );
    }

    @Test
    public void registerUserWithInvalidEmail_ShouldBeFailed() {
        String name = "Test user";
        String email = System.currentTimeMillis() + "mail.com";
        String password = "Strong123!";

        Response response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8") // charset=utf-8 !!!
                .formParam("name", name)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/users/register")
                .then()
                .extract()
                .response();

        assertAll("Health-check response validation",
                () -> assertThat(response.jsonPath().getString("message"), is("A valid email address is required")),
                () -> assertThat(response.jsonPath().getInt("status"), is(400)),
                () -> assertThat(response.jsonPath().getBoolean("success"), is(false))
        );
    }
}
