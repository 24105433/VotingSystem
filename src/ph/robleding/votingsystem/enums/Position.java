package ph.robleding.votingsystem.enums;

public enum Position {
    PRESIDENT,
    VICE_PRESIDENT,
    SENATOR,
    GOVERNOR,
    VICE_GOVERNOR,
    MAYOR,
    VICE_MAYOR,
    COUNCILOR;

    public static Position fromString(String input) {
        try {
            return Position.valueOf(input.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
