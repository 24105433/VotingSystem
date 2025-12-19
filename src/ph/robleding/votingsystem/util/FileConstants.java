package ph.robleding.votingsystem.util;

public class FileConstants {

    // 💾 Binary data files (.dat)
    public static final String VOTERS_FILE = "voters.dat";
    public static final String CANDIDATES_FILE = "candidates.dat";
    public static final String VOTES_FILE = "votes.dat";
    public static final String ELECTION_FILE = "election.dat";
    public static final String ADMINS_FILE = "admins.dat";

    // 📊 CSV export files
    public static final String VOTERS_CSV = "voters.csv";
    public static final String CANDIDATES_CSV = "candidates.csv";
    public static final String RESULTS_CSV = "results.csv";

    // Private constructor to prevent instantiation
    private FileConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}