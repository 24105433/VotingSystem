package ph.robleding.votingsystem.ui;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.*;
import ph.robleding.votingsystem.service.*;
import ph.robleding.votingsystem.util.*;

import java.util.*;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final CandidateService candidateService = new CandidateService();
    private final VoteService voteService = new VoteService(candidateService, userService);

    private final ElectionService electionService = new ElectionService();

    public ConsoleUI() {
        // ✅ Add this to link the services
        candidateService.setUserService(userService);
    }
    public void start() {
        System.out.println("🎉 Welcome to Robleding Voting System 🎉");

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Login");
            System.out.println("2. Register as Voter");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> login();
                case "2" -> registerVoter();
                case "3" -> {
                    System.out.println("👋 Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }

    private void login() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        String password = PasswordField.readPassword("Password: ");

        User user = userService.login(name, password);

        if (user == null) {
            System.out.println("❌ Login failed. User not found.");
            return;
        }

        // ✅ Only Admin and Voter roles can log in
        switch (user.getRole()) {
            case "ADMIN" -> adminMenu((Admin) user);
            case "VOTER" -> voterMenu((Voter) user);
            // ❌ Remove candidate case
            default -> System.out.println("❌ Unknown role.");
        }
    }

    private void registerVoter() {
        System.out.println("📝 Voter Registration");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Province: ");
        String province = scanner.nextLine();
        System.out.print("City/Municipality: ");
        String city = scanner.nextLine();
        System.out.print("Birth Date (YYYY-MM-DD): ");
        String birthDate = scanner.nextLine();
        String password = PasswordField.readPassword("Password: ");

        // ✅ Fixed parameter order
        boolean success = userService.registerVoter(name, province, city, birthDate, password);
        if (success) {
            System.out.println("✅ Registration successful!");
        } else {
            System.out.println("❌ A voter with the same details already exists.");
        }
    }

    private void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n🔐 Admin Menu");
            System.out.println("1. Manage Election");
            System.out.println("2. View Tally");
            System.out.println("3. Manage Candidates");
            System.out.println("4. View Voters Masterlist");
            System.out.println("5. System Maintenance");
            System.out.println("6. Logout");

            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> manageElectionMenu();
                case "2" -> viewTally();
                case "3" -> manageCandidatesMenu();
                case "4" -> viewVotersMasterlist();
                case "5" -> systemMaintenanceMenu();
                case "6" -> {
                    System.out.println("👋 Logged out.");
                    return;
                }
                default -> System.out.println("❌ Invalid option.");
            }
        }
    }

private void manageElectionMenu() {
    while (true) {
        System.out.println("\n🗳️ Manage Election");
        System.out.println("1. Start Election");
        System.out.println("2. Stop Election");
        System.out.println("3. View Election Status");
        System.out.println("4. Back to Main Menu");

        System.out.print("Choose: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> electionService.startElection();
            case "2" -> electionService.stopElection();
            case "3" -> {
                String status = electionService.isElectionOngoing() ? "🟢 ONGOING" : "🔴 STOPPED";
                System.out.println("Election Status: " + status);
            }
            case "4" -> {
                return;
            }
            default -> System.out.println("❌ Invalid option.");
        }
    }
}
private void manageCandidatesMenu() {
    while (true) {
        System.out.println("\n👥 Manage Candidates");
        System.out.println("1. View All Candidates");
        System.out.println("2. View by Position");
        System.out.println("3. View by Location");
        System.out.println("4. Add Candidate Manually");
        System.out.println("5. Disqualify Candidate");
        System.out.println("6. Back to Main Menu");

        System.out.print("Choose: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> viewAllCandidates();
            case "2" -> viewCandidatesByPosition();
            case "3" -> viewCandidatesByLocation();
            case "4" -> addCandidateByAdmin();
            case "5" -> disqualifyCandidate();
            case "6" -> {
                return;
            }
            default -> System.out.println("❌ Invalid option.");
        }
    }
}
private void viewAllCandidates() {
    List<Candidate> all = candidateService.getAllCandidates();

    if (all.isEmpty()) {
        System.out.println("❌ No candidates found.");
        return;
    }

    all.sort(Comparator
            .comparing(Candidate::getPosition)
            .thenComparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER));

    System.out.println("\n📋 All Candidates:");
    System.out.println("─".repeat(80));

    Position currentPos = null;
    for (Candidate c : all) {
        if (currentPos != c.getPosition()) {
            currentPos = c.getPosition();
            System.out.println("\n📌 " + currentPos.name().replace("_", " "));
        }

        String status = "";
        if (c.isDisqualified()) status += " [DISQUALIFIED]";
        if (c.hasWithdrawn()) status += " [WITHDRAWN]";
        if (c.hasConceded()) status += " [CONCEDED]";

        System.out.printf("   - %s | %s, %s%s\n",
                c.getName(), c.getProvince(), c.getCityOrMunicipality(), status);
    }
    System.out.println("─".repeat(80));
    System.out.printf("Total: %d candidates\n", all.size());
}
private void viewCandidatesByPosition() {
    System.out.print("Enter position (or type 'LIST' to see all positions): ");
    String input = scanner.nextLine().trim();

    if (input.equalsIgnoreCase("LIST")) {
        System.out.println("Available positions:");
        for (Position pos : Position.values()) {
            System.out.println("  - " + pos.name().replace("_", " "));
        }
        return;
    }

    Position pos = Position.fromString(input);
    if (pos == null) {
        System.out.println("❌ Invalid position.");
        return;
    }

    List<Candidate> list = candidateService.getCandidatesByPosition(pos);
    list.sort(Comparator.comparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER));

    System.out.println("\n📌 Position: " + pos.name().replace("_", " "));
    System.out.println("─".repeat(80));

    if (list.isEmpty()) {
        System.out.println("   No candidates found.");
    } else {
        for (Candidate c : list) {
            String status = "";
            if (c.isDisqualified()) status += " [DISQUALIFIED]";
            if (c.hasWithdrawn()) status += " [WITHDRAWN]";
            if (c.hasConceded()) status += " [CONCEDED]";

            System.out.printf("   - %s | %s, %s%s\n",
                    c.getName(), c.getProvince(), c.getCityOrMunicipality(), status);
        }
    }
    System.out.println("─".repeat(80));
    System.out.printf("Total: %d candidates\n", list.size());
}
private void viewCandidatesByLocation() {
    System.out.println("\n1. Filter by Province");
    System.out.println("2. Filter by City/Municipality");
    System.out.print("Choose: ");
    String choice = scanner.nextLine();

    List<Candidate> filtered;
    String locationName;

    if ("1".equals(choice)) {
        System.out.print("Enter province: ");
        String province = scanner.nextLine().trim();
        locationName = "Province: " + province;

        filtered = candidateService.getAllCandidates().stream()
                .filter(c -> c.getProvince().equalsIgnoreCase(province))
                .sorted(Comparator
                        .comparing(Candidate::getPosition)
                        .thenComparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();

    } else if ("2".equals(choice)) {
        System.out.print("Enter city/municipality: ");
        String city = scanner.nextLine().trim();
        locationName = "City/Municipality: " + city;

        filtered = candidateService.getAllCandidates().stream()
                .filter(c -> c.getCityOrMunicipality().equalsIgnoreCase(city))
                .sorted(Comparator
                        .comparing(Candidate::getPosition)
                        .thenComparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    } else {
        System.out.println("❌ Invalid option.");
        return;
    }

    if (filtered.isEmpty()) {
        System.out.println("❌ No candidates found for " + locationName);
        return;
    }

    System.out.println("\n📋 Candidates - " + locationName);
    System.out.println("─".repeat(80));

    Position currentPos = null;
    for (Candidate c : filtered) {
        if (currentPos != c.getPosition()) {
            currentPos = c.getPosition();
            System.out.println("\n📌 " + currentPos.name().replace("_", " "));
        }

        String status = "";
        if (c.isDisqualified()) status += " [DISQUALIFIED]";
        if (c.hasWithdrawn()) status += " [WITHDRAWN]";
        if (c.hasConceded()) status += " [CONCEDED]";

        System.out.printf("   - %s | %s, %s%s\n",
                c.getName(), c.getProvince(), c.getCityOrMunicipality(), status);
    }
    System.out.println("─".repeat(80));
    System.out.printf("Total: %d candidates\n", filtered.size());
}
private void systemMaintenanceMenu() {
    while (true) {
        System.out.println("\n🔧 System Maintenance");
        System.out.println("1. Rebuild Vote Tally");
        System.out.println("2. Purge All Voters & Candidates");
        System.out.println("3. Export Data to CSV");
        System.out.println("4. Back to Main Menu");

        System.out.print("Choose: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> rebuildTally();
            case "2" -> purgeAllData();
            case "3" -> exportAllData();
            case "4" -> {
                return;
            }
            default -> System.out.println("❌ Invalid option.");
        }
    }
}
// Add this method after systemMaintenanceMenu()
private void exportAllData() {
    System.out.println("📤 Exporting data to CSV files...");

    // Export voters
    VoterExporter.exportAll(userService.getVoters());

    // Export candidates
    CandidateExporter.exportAll(candidateService.getAllCandidates());

    System.out.println("✅ All data exported successfully!");
}

private void rebuildTally() {
    System.out.print("⚠️ This will recalculate all vote counts from vote records. Continue? (yes/no): ");
    String confirm = scanner.nextLine().trim().toLowerCase();

    if (!confirm.equals("yes")) {
        System.out.println("❌ Cancelled.");
        return;
    }

    candidateService.rebuildVoteCountsFromVotes(voteService.getAllVotes());
    System.out.println("✅ Vote counts rebuilt successfully!");
}


    private void viewVotersMasterlist() {
        System.out.println("\n📋 Voters Masterlist Options:");
        System.out.println("1. View Whole Country");
        System.out.println("2. View by Province");
        System.out.println("3. View by City/Municipality");
        System.out.print("Choose: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> displayVotersList(userService.getVoters(), "Whole Country");
            case "2" -> viewVotersByProvince();
            case "3" -> viewVotersByCity();
            default -> System.out.println("❌ Invalid option.");
        }
    }


    private void viewVotersByProvince() {
        System.out.print("Enter Province: ");
        String province = scanner.nextLine().trim();

        List<Voter> filtered = userService.getVoters().stream()
                .filter(v -> v.getProvince().equalsIgnoreCase(province))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("❌ No voters found in province: " + province);
            return;
        }

        displayVotersList(filtered, "Province: " + province);
    }

    private void viewVotersByCity() {
        System.out.print("Enter City/Municipality: ");
        String city = scanner.nextLine().trim();

        List<Voter> filtered = userService.getVoters().stream()
                .filter(v -> v.getCityOrMunicipality().equalsIgnoreCase(city))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("❌ No voters found in city/municipality: " + city);
            return;
        }

        displayVotersList(filtered, "City/Municipality: " + city);
    }

    private void displayVotersList(List<Voter> voters, String title) {
        if (voters.isEmpty()) {
            System.out.println("❌ No voters found.");
            return;
        }

        // Sort by province, city/municipality, last name
        List<Voter> sortedVoters = new ArrayList<>(voters);
        sortedVoters.sort(Comparator
                .comparing(Voter::getProvince, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Voter::getCityOrMunicipality, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(v -> extractLastName(v.getName()), String.CASE_INSENSITIVE_ORDER)
        );

        System.out.println("\n📋 Voters Masterlist - " + title);
        System.out.println("─".repeat(80));
        System.out.printf("%-25s | %-15s | %-20s | %-12s | %s\n",
                "Name", "Province", "City/Municipality", "Birth Date", "Has Voted");
        System.out.println("─".repeat(80));

        for (Voter v : sortedVoters) {
            // Check if this voter is also a candidate
            String candidateInfo = "";
            List<Candidate> candidates = candidateService.getAllCandidates();
            for (Candidate c : candidates) {
                if (c.getName().equalsIgnoreCase(v.getName()) &&
                        c.getBirthDate().equals(v.getBirthDate())) {
                    candidateInfo = " [Candidate: " + c.getPosition() + "]";
                    break;
                }
            }

            System.out.printf("%-25s | %-15s | %-20s | %-12s | %-10s%s\n",
                    v.getName(),
                    v.getProvince(),
                    v.getCityOrMunicipality(),
                    v.getBirthDate(),
                    v.hasVoted() ? "YES" : "NO",
                    candidateInfo
            );
        }
        System.out.println("─".repeat(80));
        System.out.printf("Total Voters: %d\n", sortedVoters.size());
    }

    private String extractLastName(String fullName) {
        if (fullName == null || fullName.isBlank()) return "";
        String[] parts = fullName.trim().split("\\s+");
        return parts[parts.length - 1]; // Last word is assumed to be last name
    }
    // Add this helper method near the bottom of ConsoleUI class, after extractLastName()

    private String normalizeCityName(String city) {
        if (city == null || city.isBlank()) return "";

        // Convert to lowercase and trim
        String normalized = city.toLowerCase().trim();

        // Remove common suffixes like "city", "municipality"
        normalized = normalized
                .replace(" city", "")
                .replace(" municipality", "")
                .replace("city", "")
                .replace("municipality", "")
                .trim();

        return normalized;
    }


    private void purgeAllData() {
        System.out.print("⚠️ Are you sure you want to DELETE ALL VOTERS and CANDIDATES? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("yes")) {
            System.out.println("❌ Purge cancelled.");
            return;
        }

        boolean votersDeleted = FileUtil.deleteFile("voters.dat");
        boolean candidatesDeleted = FileUtil.deleteFile("candidates.dat");
        boolean votersCSVDeleted = FileUtil.deleteFile("voters.csv");
        boolean candidatesCSVDeleted = FileUtil.deleteFile("candidates.csv");

        System.out.println("✅ Data purge complete.");
        System.out.println(votersDeleted ? "🧹 voters.dat deleted" : "⚠️ voters.dat not found");
        System.out.println(candidatesDeleted ? "🧹 candidates.dat deleted" : "⚠️ candidates.dat not found");
        System.out.println(votersCSVDeleted ? "🧹 voters.csv deleted" : "⚠️ voters.csv not found");
        System.out.println(candidatesCSVDeleted ? "🧹 candidates.csv deleted" : "⚠️ candidates.csv not found");

        System.out.println("🔄 Restart app to fully refresh state.");
    }

    private void addCandidateByAdmin() {
        System.out.println("📝 Manually Add a Candidate");

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Province: ");
        String province = scanner.nextLine();

        System.out.print("City/Municipality: ");
        String city = scanner.nextLine();

        System.out.print("Birth Date (YYYY-MM-DD): ");
        String birthDate = scanner.nextLine();

        String password = PasswordField.readPassword("Password: ");

        System.out.print("Enter position to run for: ");
        Position pos = Position.fromString(scanner.nextLine());
        if (pos == null) {
            System.out.println("❌ Invalid position.");
            return;
        }

        System.out.print("Enter campaign location: ");
        String location = scanner.nextLine();

        Candidate newCandidate = new Candidate(name, province, city, birthDate, password, pos, location);
        candidateService.addCandidate(newCandidate);
        CandidateExporter.appendCandidate(newCandidate);

        System.out.println("✅ Candidate added successfully.");
    }

    private void disqualifyCandidate() {
        System.out.print("Enter candidate name to disqualify: ");
        String name = scanner.nextLine();
        System.out.print("Enter position: ");
        Position pos = Position.fromString(scanner.nextLine());

        if (pos == null) {
            System.out.println("❌ Invalid position.");
            return;
        }

        candidateService.disqualify(name, pos);
        System.out.println("✅ Candidate disqualified.");
    }

    private void viewTally() {
        System.out.println("\n📊 Tally Menu:");
        System.out.println("1. View all positions (nationwide)");
        System.out.println("2. View specific position");
        System.out.println("3. View by province");
        System.out.println("4. View by city/municipality");
        System.out.print("Choose: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> {
                for (Position pos : Position.values()) {
                    printTallyFor(pos, null, null);
                }
            }
            case "2" -> {
                System.out.print("Enter position: ");
                Position pos = Position.fromString(scanner.nextLine());
                if (pos != null) printTallyFor(pos, null, null);
                else System.out.println("❌ Invalid position.");
            }
            case "3" -> viewTallyByProvince();
            case "4" -> viewTallyByCity();
            default -> System.out.println("❌ Invalid option.");
        }
    }

    private void viewTallyByProvince() {
        System.out.print("Enter province: ");
        String province = scanner.nextLine().trim();

        System.out.println("\n📊 Tally for Province: " + province);
        System.out.println("═".repeat(60));

        for (Position pos : Position.values()) {
            printTallyFor(pos, province, null);
        }
    }

    private void viewTallyByCity() {
        System.out.print("Enter province: ");
        String province = scanner.nextLine().trim();
        System.out.print("Enter city/municipality: ");
        String city = scanner.nextLine().trim();

        System.out.println("\n📊 Tally for " + city + ", " + province);
        System.out.println("═".repeat(60));

        for (Position pos : Position.values()) {
            printTallyFor(pos, province, city);
        }
    }

    private void printTallyFor(Position pos, String province, String city) {
        List<Candidate> list = candidateService.getCandidatesByPosition(pos);

        // Filter by location if specified
        if (province != null) {
            list = list.stream()
                    .filter(c -> c.getProvince().equalsIgnoreCase(province))
                    .toList();
        }
        if (city != null) {
            // Flexible city matching
            String normalizedSearchCity = normalizeCityName(city);
            list = list.stream()
                    .filter(c -> normalizeCityName(c.getCityOrMunicipality()).equals(normalizedSearchCity))
                    .toList();
        }

        // Only show positions that have candidates
        if (list.isEmpty()) {
            return;
        }

        // ✅ Sort by votes (highest first), then by name alphabetically
        list = list.stream()
                .sorted(Comparator
                        .comparingInt(Candidate::getVotes).reversed()  // Most votes first
                        .thenComparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER))  // Then alphabetically
                .toList();

        System.out.println("\n📌 Position: " + pos);
        for (Candidate c : list) {
            String location = "";
            if (pos == Position.MAYOR || pos == Position.VICE_MAYOR ||
                    pos == Position.COUNCILOR) {
                location = " (" + c.getCityOrMunicipality() + ")";
            } else if (pos == Position.GOVERNOR || pos == Position.VICE_GOVERNOR) {
                location = " (" + c.getProvince() + ")";
            }
            System.out.printf("   - %s%s: %d votes\n", c.getName(), location, c.getVotes());
        }
    }

    private void voterMenu(Voter voter) {
        while (true) {
            System.out.println("\n🗳️ Voter Menu");
            System.out.println("1. Vote");
            System.out.println("2. Submit Candidacy");
            System.out.println("3. View Receipt");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleVoting(voter);
                case "2" -> handleCandidacy(voter);
                case "3" -> printReceipt(voter);
                case "4" -> {
                    System.out.println("👋 Logged out.");
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }

// Replace your entire handleVoting method with this:
private void handleVoting(Voter voter) {
    if (!electionService.isElectionOngoing()) {
        System.out.println("⚠️ Election is not active.");
        return;
    }

    if (voteService.hasVoted(voter)) {
        System.out.println("⚠️ You have already voted.");
        return;
    }

    Map<Position, String> selections = new HashMap<>();
    System.out.println("\n🗳️ BALLOT");
    System.out.println("═".repeat(70));
    System.out.println("Enter the NUMBER of your choice (or press Enter to skip)");
    System.out.println("For SENATOR and COUNCILOR, you can select multiple candidates.");
    System.out.println("═".repeat(70));

    for (Position pos : Position.values()) {
        List<Candidate> candidates = candidateService.getCandidatesByPosition(pos)
                .stream()

                .filter(c -> {
                    if (pos == Position.PRESIDENT || pos == Position.VICE_PRESIDENT || pos == Position.SENATOR) {
                        return true;
                    } else if (pos == Position.GOVERNOR || pos == Position.VICE_GOVERNOR) {
                        return c.getProvince().equalsIgnoreCase(voter.getProvince());
                    } else {
                        // ✅ UPDATED: Flexible city matching
                        String voterCity = normalizeCityName(voter.getCityOrMunicipality());
                        String candidateCity = normalizeCityName(c.getCityOrMunicipality());

                        return c.getProvince().equalsIgnoreCase(voter.getProvince())
                                && candidateCity.equals(voterCity);
                    }
                })
                .sorted(Comparator.comparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();

        if (candidates.isEmpty()) {
            continue;
        }

        System.out.println("\n📌 " + pos.name().replace("_", " "));
        System.out.println("─".repeat(70));

        // Display numbered candidates
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            System.out.printf("  [%d] %s (%s)\n", (i + 1), c.getName(), c.getLocation());
        }

        // Handle voting based on position type
        if (pos == Position.SENATOR) {
            handleMultipleSelection(pos, candidates, selections, 12, "senators");
        } else if (pos == Position.COUNCILOR) {
            handleMultipleSelection(pos, candidates, selections, 10, "councilors");
        } else {
            handleSingleSelection(pos, candidates, selections);
        }
    }

    if (selections.isEmpty()) {
        System.out.println("\n❌ No votes cast. Ballot is empty.");
        return;
    }

    // Show summary and confirm
    // In handleVoting method, replace the vote summary section with this:

// Show summary and confirm
    System.out.println("\n📋 VOTE SUMMARY:");
    System.out.println("═".repeat(70));

// ✅ Display in proper hierarchy order
    Position[] hierarchyOrder = {
            Position.PRESIDENT,
            Position.VICE_PRESIDENT,
            Position.SENATOR,
            Position.GOVERNOR,
            Position.VICE_GOVERNOR,
            Position.MAYOR,
            Position.VICE_MAYOR,
            Position.COUNCILOR
    };

    for (Position pos : hierarchyOrder) {
        if (selections.containsKey(pos)) {
            System.out.printf("%-20s: %s\n", pos.name().replace("_", " "), selections.get(pos));
        }
    }

    System.out.println("═".repeat(70));
    System.out.print("Confirm your vote? (yes/no): ");
    String confirm = scanner.nextLine().trim().toLowerCase();

    if (!confirm.equals("yes")) {
        System.out.println("❌ Vote cancelled.");
        return;
    }

    boolean success = voteService.castVote(voter, selections, candidateService.getAllCandidates());

    if (success) {
        System.out.println("✅ Vote cast successfully!");
    } else {
        System.out.println("❌ Vote failed. Please try again.");
    }
}

private void handleSingleSelection(Position pos, List<Candidate> candidates, Map<Position, String> selections) {
    System.out.print("Your choice (number or press Enter to skip): ");
    String input = scanner.nextLine().trim();

    if (!input.isEmpty()) {
        try {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= candidates.size()) {
                selections.put(pos, candidates.get(choice - 1).getName());
                System.out.println("✓ Selected: " + candidates.get(choice - 1).getName());
            } else {
                System.out.println("❌ Invalid number. Skipping this position.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Skipping this position.");
        }
    }
}

private void handleMultipleSelection(Position pos, List<Candidate> candidates,
                                     Map<Position, String> selections, int maxSelections, String positionName) {
    System.out.printf("Select up to %d %s (enter numbers separated by commas, e.g., 1,3,5): ", maxSelections, positionName);
    String input = scanner.nextLine().trim();

    if (input.isEmpty()) {
        return;
    }

    String[] choices = input.split(",");
    List<String> selectedNames = new ArrayList<>();
    Set<Integer> selectedNumbers = new HashSet<>();

    for (String choice : choices) {
        try {
            int num = Integer.parseInt(choice.trim());
            if (num >= 1 && num <= candidates.size() && !selectedNumbers.contains(num)) {
                selectedNumbers.add(num);
                selectedNames.add(candidates.get(num - 1).getName());
            }
        } catch (NumberFormatException e) {
            // Skip invalid input
        }
    }

    if (selectedNames.size() > maxSelections) {
        System.out.printf("⚠️ You selected %d candidates. Only the first %d will be counted.\n",
                selectedNames.size(), maxSelections);
        selectedNames = selectedNames.subList(0, maxSelections);
    }

    if (!selectedNames.isEmpty()) {
        // Store multiple selections as comma-separated string
        selections.put(pos, String.join(", ", selectedNames));
        System.out.printf("✓ Selected %d %s: %s\n", selectedNames.size(), positionName, String.join(", ", selectedNames));
    }
}

    private void handleCandidacy(Voter voter) {
        System.out.print("Enter position to run for: ");
        Position pos = Position.fromString(scanner.nextLine());
        if (pos == null) {
            System.out.println("❌ Invalid position.");
            return;
        }

        System.out.print("Enter your campaign location: ");
        String location = scanner.nextLine();

        boolean success = candidateService.submitCandidacy(voter, pos, location);
        if (success) {
            System.out.println("✅ Candidacy submitted!");
        } else {
            System.out.println("❌ Already running for this position.");
        }
    }

    private void printReceipt(Voter voter) {
        Vote receipt = voteService.getReceipt(voter);
        if (receipt != null) {
            receipt.printReceipt();
        } else {
            System.out.println("⚠️ No vote record found.");
        }
    }

    private void viewCandidates() {
        System.out.print("Enter position to filter by (or type 'ALL' to view all): ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("ALL")) {
            for (Position pos : Position.values()) {
                printCandidatesFor(pos);
            }
        } else {
            Position pos = Position.fromString(input);
            if (pos == null) {
                System.out.println("❌ Invalid position.");
            } else {
                printCandidatesFor(pos);
            }
        }
    }

    private void printCandidatesFor(Position pos) {
        List<Candidate> list = candidateService.getCandidatesByPosition(pos);
        System.out.println("\n📌 Position: " + pos);
        if (list.isEmpty()) {
            System.out.println(" - No candidates currently running.");
        } else {
            for (Candidate c : list) {
                String flags = "";
                if (c.isDisqualified()) flags += " (Disqualified)";
                if (c.hasWithdrawn()) flags += " (Withdrawn)";
                if (c.hasConceded()) flags += " (Conceded)";
                System.out.printf(" - %s (%s)%s\n", c.getName(), c.getLocation(), flags);
            }
        }
    }

    private void candidateMenu(Candidate candidate) {
        while (true) {
            System.out.println("\n🏃 Candidate Menu");
            System.out.println("1. View Votes");
            System.out.println("2. Withdraw from Race");
            System.out.println("3. Concede");
            System.out.println("4. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> System.out.printf("📊 Current votes: %d\n", candidate.getVotes());
                case "2" -> {
                    candidateService.withdraw(candidate.getName(), candidate.getPosition());
                    System.out.println("✅ Withdrawn.");
                }
                case "3" -> {
                    candidateService.concede(candidate.getName(), candidate.getPosition());
                    System.out.println("✅ You have conceded.");
                }
                case "4" -> {
                    System.out.println("👋 Logged out.");
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
}
