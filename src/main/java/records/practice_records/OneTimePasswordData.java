package records.practice_records;

/**
 * Data object representing an OTP (One-Time Password) login attempt.
 */

public record OneTimePasswordData(String email, String otpCode) {
    public static OneTimePasswordData defaultOtp() {
        return new OneTimePasswordData("practice@expandtesting.com", "214365");
    }
}
