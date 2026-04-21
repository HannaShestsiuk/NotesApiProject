package helpers;

import com.github.javafaker.Faker;
import enums.NoteCategory;
import records.NoteWithStatus;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        return faker.internet().emailAddress();
    }

    public static String randomPassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength);
    }

    public static String randomPhone(int length) {
        return faker.number().digits(length);
    }

    public static String randomCompany() {
        return faker.company().industry();
    }

    public static String randomCompany(int length) {
        return faker.lorem().characters(length);
    }

    public static String randomName() {
        return "user" + System.currentTimeMillis();
    }

    public static String randomNumber() {
        return String.valueOf(faker.number().numberBetween(0, 1000000));
    }

    public static String randomString() {
        return String.valueOf(faker.lorem().word());
    }

    public static String randomDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return faker.date().past(365, TimeUnit.DAYS)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(dtf);
    }

    public static List<NoteWithStatus> generateRandomNotes(int count) {
        List<NoteWithStatus> notes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            notes.add(new NoteWithStatus(
                    "Bulk Note " + (i + 1) + " " + randomTitle(3),
                    randomDescription(),
                    false,
                    NoteCategory.random().getLabel()
            ));
        }
        return notes;
    }

}
