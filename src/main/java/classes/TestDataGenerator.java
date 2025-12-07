package classes;

import com.github.javafaker.Faker;

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

    public static String randomEmail() {
        return faker.internet().emailAddress();
    }

    public static String randomPassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength);
    }
}
