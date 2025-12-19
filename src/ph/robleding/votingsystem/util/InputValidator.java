package ph.robleding.votingsystem.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.Period;

public class InputValidator {

    // ✅ Date validation
    public static boolean isValidDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(date); // Uses ISO format YYYY-MM-DD
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // ✅ Check if voter is 18+ years old
    public static boolean isValidVotingAge(String birthDate) {
        if (!isValidDate(birthDate)) {
            return false;
        }
        LocalDate birth = LocalDate.parse(birthDate);
        LocalDate today = LocalDate.now();
        int age = Period.between(birth, today).getYears();
        return age >= 18;
    }

    // ✅ Name validation
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Allow letters, spaces, hyphens, and apostrophes
        // Example: "Jose Rizal", "Mary-Jane Santos", "O'Brien"
        return name.trim().length() >= 2 &&
                name.matches("[a-zA-Z\\s\\-']+");
    }

    // ✅ Location validation
    public static boolean isValidLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return false;
        }
        // Allow letters, spaces, periods (for abbreviations like "Mt. Province")
        return location.trim().length() >= 2 &&
                location.matches("[a-zA-Z\\s.]+");
    }

    // ✅ Password validation
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        // Minimum 6 characters for security
        return password.length() >= 6;
    }

    // ✅ Number input validation
    public static boolean isValidNumber(String input, int min, int max) {
        try {
            int num = Integer.parseInt(input);
            return num >= min && num <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ✅ Helper method to display validation error messages
    public static void printValidationError(String field, String requirement) {
        System.out.println("❌ Invalid " + field + ": " + requirement);
    }
}