package ph.robleding.votingsystem.util;

public class ElectionConstants {

    // 🗳️ Vote limits
    public static final int MAX_SENATORS = 12;
    public static final int MAX_COUNCILORS = 10;

    // 📏 Input constraints
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_VOTING_AGE = 18;

    // 📋 Display formatting
    public static final int SEPARATOR_LENGTH = 80;
    public static final String SEPARATOR_CHAR = "═";
    public static final String LIGHT_SEPARATOR_CHAR = "─";

    // Private constructor to prevent instantiation
    private ElectionConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}