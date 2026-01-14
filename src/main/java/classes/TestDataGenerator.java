package classes;

import com.github.javafaker.Faker;

import java.util.UUID;

public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static String randomTitle() {
        return faker.lorem().sentence();
    }

    public static String randomTitle(int length) {
        return faker.lorem().characters(length);
    }

    public static String randomDescription() {
        return faker.lorem().sentence();
    }

    public static String randomDescription(int length) {
        return faker.lorem().characters(length);
    }

    public static String randomUserName() {
        return faker.name().fullName();
    }

    public static String randomUserName(int length) {
        return faker.lorem().characters(length);
    }

    public static String randomEmail() {
        return UUID.randomUUID() + "@mail.com";
    }

    public static String randomPassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength);
    }

    public static String randomPhone(int length) {
        return faker.number().digits(length);
    }

    public static String randomCompany() {
        String value = faker.company().industry();
        return value.length() > 30 ? value.substring(0, 30) : value;
    }

    public static String randomCompany(int length) {
        return faker.lorem().characters(length);
    }
}
