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
        //  Link CandidateService to UserService (existing)
        candidateService.setUserService(userService);

        // NEW - Link ElectionService to all services for vote clearing
        electionService.setServices(candidateService, voteService, userService);
    }
    public void start() {
        System.out.println("\n\n=== Welcome to Robleding Voting System ===");

        while (true) {
            System.out.println("\n\nMain Menu:");
            System.out.println("\n1. Login");
            System.out.println("2. Register as Voter");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> login();
                case "2" -> registerVoter();
                case "3" -> {
                    System.out.println("\n\n===Goodbye!===");
                    return;
                }
                default -> System.out.println("===Invalid choice.===");
            }
        }
    }

    private void login() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        String password = PasswordField.readPassword("Password: ");

        User user = userService.login(name, password);

        if (user == null) {
            System.out.println(" Login failed. User not found.");
            return;
        }

        //  Only Admin and Voter roles can log in
        switch (user.getRole()) {
            case "ADMIN" -> adminMenu((Admin) user);
            case "VOTER" -> voterMenu((Voter) user);
            //  Remove candidate case
            default -> System.out.println(" Unknown role.");
        }
    }

    private void registerVoter() {
        System.out.println("===Voter Registration===");

        System.out.print("Name: ");
        String name = scanner.nextLine();
        if (!InputValidator.isValidName(name)) {
            InputValidator.printValidationError("name", "Must be at least 2 characters, letters only");
            return;
        }

        System.out.print("Province: ");
        String province = scanner.nextLine();
        if (!InputValidator.isValidLocation(province)) {
            InputValidator.printValidationError("province", "Must be at least 2 characters");
            return;
        }

        System.out.print("City/Municipality: ");
        String city = scanner.nextLine();
        if (!InputValidator.isValidLocation(city)) {
            InputValidator.printValidationError("city/municipality", "Must be at least 2 characters");
            return;
        }

        System.out.print("Birth Date (YYYY-MM-DD): ");
        String birthDate = scanner.nextLine();
        if (!InputValidator.isValidDate(birthDate)) {
            InputValidator.printValidationError("birth date", "Must be in YYYY-MM-DD format");
            return;
        }
        if (!InputValidator.isValidVotingAge(birthDate)) {
            System.out.println(" You must be at least 18 years old to register.");
            return;
        }

        String password = PasswordField.readPassword("Password: ");
        if (!InputValidator.isValidPassword(password)) {
            InputValidator.printValidationError("password", "Must be at least 6 characters");
            return;
        }

        boolean success = userService.registerVoter(name, province, city, birthDate, password);
        if (success) {
            System.out.println("Registration successful!");
        } else {
            System.out.println(" A voter with the same details already exists.");
        }
    }

    private void adminMenu(Admin admin) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" Welcome, " + admin.getName() + "!");
        System.out.println("Role: Administrator");
        System.out.println("=".repeat(60));
        if (electionService.isElectionOngoing()) {
            System.out.println("Election Status: ONGOING");
        } else {
            System.out.println("Election Status:  STOPPED");
        }
        while (true) {
            System.out.println("\n Admin Menu");
            System.out.println("1. Manage Election");
            System.out.println("2. View Tally");
            System.out.println("3. Manage Candidates");
            System.out.println("4. View Voters Masterlist");
            System.out.println("5. System Maintenance");
            System.out.println("6. View Raw Data Files"); //  NEW
            System.out.println("7. Logout");

            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> manageElectionMenu();
                case "2" -> viewTally();
                case "3" -> manageCandidatesMenu();
                case "4" -> viewVotersMasterlist();
                case "5" -> systemMaintenanceMenu();
                case "6" -> viewRawDataMenu(); //  NEW
                case "7" -> {
                    System.out.println(" Logged out.");
                    return;
                }
                default -> System.out.println(" Invalid option.");
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
            case "1" -> startElectionWithConfirmation();  //  NEW
            case "2" -> electionService.stopElection();
            case "3" -> {
                String status = electionService.isElectionOngoing() ? "🟢 ONGOING" : "STOPPED";
                System.out.println("Election Status: " + status);
            }
            case "4" -> {
                return;
            }
            default -> System.out.println("Invalid option.");
        }
    }
}
    private void startElectionWithConfirmation() {
        long voteCount = voteService.getAllVotes().size();

        if (voteCount > 0) {
            System.out.println("\n WARNING: There are " + voteCount + " votes from the previous election.");
            System.out.println("Starting a new election will PERMANENTLY DELETE these votes.");
            System.out.print("\nDo you want to continue? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (!confirm.equals("yes")) {
                System.out.println(" Election start cancelled. Previous results are preserved.");
                return;
            }
        }

        electionService.startElection();
    }
private void manageCandidatesMenu() {
    while (true) {
        System.out.println("\n Manage Candidates");
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
            default -> System.out.println(" Invalid option.");
        }
    }
}
private void viewAllCandidates() {
    List<Candidate> all = candidateService.getAllCandidates();

    if (all.isEmpty()) {
        System.out.println(" No candidates found.");
        return;
    }

    all.sort(Comparator
            .comparing(Candidate::getPosition)
            .thenComparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER));

    System.out.println("\n All Candidates:");
    System.out.println("─".repeat(80));

    Position currentPos = null;
    for (Candidate c : all) {
        if (currentPos != c.getPosition()) {
            currentPos = c.getPosition();
            System.out.println("\n! " + currentPos.name().replace("_", " "));
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
        System.out.println("Invalid position.");
        return;
    }

    List<Candidate> list = candidateService.getCandidatesByPosition(pos);
    list.sort(Comparator.comparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER));

    System.out.println("\nPosition: " + pos.name().replace("_", " "));
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
        System.out.println("Invalid option.");
        return;
    }

    if (filtered.isEmpty()) {
        System.out.println("No candidates found for " + locationName);
        return;
    }

    System.out.println("\nCandidates - " + locationName);
    System.out.println("─".repeat(80));

    Position currentPos = null;
    for (Candidate c : filtered) {
        if (currentPos != c.getPosition()) {
            currentPos = c.getPosition();
            System.out.println("\n " + currentPos.name().replace("_", " "));
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
        System.out.println("\n ===System Maintenance===");
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
            default -> System.out.println(" Invalid option.");
        }
    }
}
// Add this method after systemMaintenanceMenu()
private void exportAllData() {
    System.out.println("Exporting data to CSV files...");

    // Export voters
    VoterExporter.exportAll(userService.getVoters());

    // Export candidates
    CandidateExporter.exportAll(candidateService.getAllCandidates());

    System.out.println("All data exported successfully!");
}

private void rebuildTally() {
    System.out.print(" This will recalculate all vote counts from vote records. Continue? (yes/no): ");
    String confirm = scanner.nextLine().trim().toLowerCase();

    if (!confirm.equals("yes")) {
        System.out.println(" Cancelled.");
        return;
    }

    candidateService.rebuildVoteCountsFromVotes(voteService.getAllVotes());
    System.out.println(" Vote counts rebuilt successfully!");
}


    private void viewVotersMasterlist() {
        userService.reload();
        System.out.println("\n Voters Masterlist Options:");
        System.out.println("1. View Whole Country");
        System.out.println("2. View by Province");
        System.out.println("3. View by City/Municipality");
        System.out.print("Choose: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> displayVotersList(userService.getVoters(), "Whole Country");
            case "2" -> viewVotersByProvince();
            case "3" -> viewVotersByCity();
            default -> System.out.println("Invalid option.");
        }
    }


    private void viewVotersByProvince() {
        userService.reload();
        System.out.print("Enter Province: ");
        String province = scanner.nextLine().trim();

        List<Voter> filtered = userService.getVoters().stream()
                .filter(v -> v.getProvince().equalsIgnoreCase(province))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("No voters found in province: " + province);
            return;
        }

        displayVotersList(filtered, "Province: " + province);
    }

    private void viewVotersByCity() {
        userService.reload();
        System.out.print("Enter City/Municipality: ");
        String city = scanner.nextLine().trim();

        List<Voter> filtered = userService.getVoters().stream()
                .filter(v -> v.getCityOrMunicipality().equalsIgnoreCase(city))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("No voters found in city/municipality: " + city);
            return;
        }

        displayVotersList(filtered, "City/Municipality: " + city);
    }

    private void displayVotersList(List<Voter> voters, String title) {
        if (voters.isEmpty()) {
            System.out.println(" No voters found.");
            System.out.println(ElectionConstants.SEPARATOR_CHAR.repeat(ElectionConstants.SEPARATOR_LENGTH));
            return;
        }

        // Sort by province, city/municipality, last name
        List<Voter> sortedVoters = new ArrayList<>(voters);
        sortedVoters.sort(Comparator
                .comparing(Voter::getProvince, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Voter::getCityOrMunicipality, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(v -> extractLastName(v.getName()), String.CASE_INSENSITIVE_ORDER)
        );
        userService.reload();
        System.out.println("\n Voters Masterlist - " + title);
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
        System.out.print("Are you sure you want to DELETE ALL VOTERS and CANDIDATES? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("yes")) {
            System.out.println(" Purge cancelled.");
            return;
        }

        boolean votersDeleted = FileUtil.deleteFile(FileConstants.VOTERS_FILE);
        boolean candidatesDeleted = FileUtil.deleteFile(FileConstants.CANDIDATES_FILE);
        boolean votersCSVDeleted = FileUtil.deleteFile(FileConstants.VOTERS_CSV);
        boolean candidatesCSVDeleted = FileUtil.deleteFile(FileConstants.CANDIDATES_CSV);

        System.out.println(" Data purge complete.");
        System.out.println(votersDeleted ? " voters.dat deleted" : " voters.dat not found");
        System.out.println(candidatesDeleted ? " candidates.dat deleted" : " candidates.dat not found");
        System.out.println(votersCSVDeleted ? " voters.csv deleted" : "voters.csv not found");
        System.out.println(candidatesCSVDeleted ? " candidates.csv deleted" : "candidates.csv not found");
        // ... rest of code

        System.out.println(" Restart app to fully refresh state.");
    }

    private void addCandidateByAdmin() {
        if (electionService.isElectionOngoing()) {
            System.out.println(" Cannot add candidates while election is ongoing.");
            return;
        }
        System.out.println("Manually Add a Candidate");

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
            System.out.println("Invalid position.");
            return;
        }

        System.out.print("Enter campaign location: ");
        String location = scanner.nextLine();

        Candidate newCandidate = new Candidate(name, province, city, birthDate, password, pos, location);
        candidateService.addCandidate(newCandidate);
        CandidateExporter.appendCandidate(newCandidate);

        System.out.println("Candidate added successfully.");
    }

    private void disqualifyCandidate() {
        if (electionService.isElectionOngoing()) {
            System.out.println("Cannot disqualify candidates while election is ongoing.");
            return;
        }
        System.out.print("Enter candidate name to disqualify: ");
        String name = scanner.nextLine();
        System.out.print("Enter position: ");
        Position pos = Position.fromString(scanner.nextLine());

        if (pos == null) {
            System.out.println("Invalid position.");
            return;
        }

        candidateService.disqualify(name, pos);
        System.out.println(" Candidate disqualified.");
    }

    private void viewTally() {
        candidateService.reload();
        System.out.println("\n Tally Menu:");
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
                else System.out.println("Invalid position.");
            }
            case "3" -> viewTallyByProvince();
            case "4" -> viewTallyByCity();
            default -> System.out.println("Invalid option.");
        }
    }

    private void viewTallyByProvince() {
        candidateService.reload();
        System.out.print("Enter province: ");
        String province = scanner.nextLine().trim();

        System.out.println("\nTally for Province: " + province);
        System.out.println("═".repeat(60));

        for (Position pos : Position.values()) {
            printTallyFor(pos, province, null);
        }
    }

    private void viewTallyByCity() {
        candidateService.reload();
        System.out.print("Enter province: ");
        String province = scanner.nextLine().trim();
        System.out.print("Enter city/municipality: ");
        String city = scanner.nextLine().trim();

        System.out.println("\nTally for " + city + ", " + province);
        System.out.println("═".repeat(60));

        for (Position pos : Position.values()) {
            printTallyFor(pos, province, city);
        }
    }

    private void printTallyFor(Position pos, String province, String city) {
        candidateService.reload();
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

        // Sort by votes (highest first), then by name alphabetically
        list = list.stream()
                .sorted(Comparator
                        .comparingInt(Candidate::getVotes).reversed()  // Most votes first
                        .thenComparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER))  // Then alphabetically
                .toList();

        System.out.println("\n Position: " + pos);
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
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" Welcome, " + voter.getName() + "!");
        System.out.println("Location: " + voter.getCityOrMunicipality() + ", " + voter.getProvince());
        // Show voting status
        if (voter.hasVoted()) {
            System.out.println("Status:  You have already voted in this election");
        } else if (electionService.isElectionOngoing()) {
            System.out.println("Status:  You can vote now - Election is ongoing");
        } else {
            System.out.println("Status:   Election is not active");
        }
        System.out.println("=".repeat(60));
        while (true) {
            System.out.println("\n Voter Menu");
            System.out.println("1. Vote");
            System.out.println("2. View Receipt");
            System.out.println("3. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleVoting(voter);
                case "2" -> printReceipt(voter);
                case "3" -> {
                    System.out.println(" Logged out.");
                    return;
                }
                default -> System.out.println(" Invalid choice.");
            }
        }
    }

// Replace your entire handleVoting method with this:
private void handleVoting(Voter voter) {
    if (!electionService.isElectionOngoing()) {
        System.out.println(" Election is not active.");
        return;
    }

    if (voteService.hasVoted(voter)) {
        System.out.println(" You have already voted.");
        return;
    }

    Map<Position, String> selections = new HashMap<>();
    System.out.println("\n BALLOT");
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
                        // Flexible city matching
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

        System.out.println("\n " + pos.name().replace("_", " "));
        System.out.println("─".repeat(70));

        // Display numbered candidates
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            System.out.printf("  [%d] %s (%s)\n", (i + 1), c.getName(), c.getLocation());
        }

        // Handle voting based on position type
        if (pos == Position.SENATOR) {
            handleMultipleSelection(pos, candidates, selections,
                    ElectionConstants.MAX_SENATORS, "senators");
        } else if (pos == Position.COUNCILOR) {
            handleMultipleSelection(pos, candidates, selections,
                    ElectionConstants.MAX_COUNCILORS, "councilors");
        } else {
            // All other positions: President, Vice President, Governor, Vice Governor, Mayor, Vice Mayor
            handleSingleSelection(pos, candidates, selections);
        }
    }

    if (selections.isEmpty()) {
        System.out.println("\nNo votes cast. Ballot is empty.");
        return;
    }

    // Show summary and confirm
    System.out.println("\n VOTE SUMMARY:");
    System.out.println("═".repeat(70));

    // Display in proper hierarchy order
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
        System.out.println("Vote cancelled.");
        return;
    }

    boolean success = voteService.castVote(voter, selections, candidateService.getAllCandidates());

    if (success) {
        System.out.println(" Vote cast successfully!");
    } else {
        System.out.println(" Vote failed. Please try again.");
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
        System.out.printf(" You selected %d candidates. Only the first %d will be counted.\n",
                selectedNames.size(), maxSelections);
        selectedNames = selectedNames.subList(0, maxSelections);
    }

    if (!selectedNames.isEmpty()) {
        // Store multiple selections as comma-separated string
        selections.put(pos, String.join(", ", selectedNames));
        System.out.printf("✓ Selected %d %s: %s\n", selectedNames.size(), positionName, String.join(", ", selectedNames));
    }
}



    private void printReceipt(Voter voter) {
        Vote receipt = voteService.getReceipt(voter);
        if (receipt != null) {
            receipt.printReceipt();
        } else {
            System.out.println("No vote record found.");
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
                System.out.println(" Invalid position.");
            } else {
                printCandidatesFor(pos);
            }
        }
    }
    private void viewRawDataMenu() {
        while (true) {
            System.out.println("\n View Raw Data Files");
            System.out.println("1. View All Voters with Passwords");
            System.out.println("2. View All Admins with Passwords");
            System.out.println("3. View All Candidates Data");
            System.out.println("4. View All Votes");
            System.out.println("5. View Election State");
            System.out.println("6. Back to Admin Menu");

            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> displayVotersRawData();
                case "2" -> displayAdminsRawData();
                case "3" -> displayCandidatesRawData();
                case "4" -> displayVotesRawData();
                case "5" -> displayElectionState();
                case "6" -> {
                    return;
                }
                default -> System.out.println(" Invalid option.");
            }
        }
    }

    private void displayVotersRawData() {
        List<Voter> voters = userService.getVoters();

        System.out.println("\n ALL VOTERS (voters.dat)");
        System.out.println("═".repeat(100));
        System.out.printf("%-25s | %-15s | %-20s | %-12s | %-10s | %-20s\n",
                "Name", "Province", "City", "Birth Date", "Has Voted", "Password");
        System.out.println("─".repeat(100));

        for (Voter v : voters) {
            System.out.printf("%-25s | %-15s | %-20s | %-12s | %-10s | %-20s\n",
                    v.getName(),
                    v.getProvince(),
                    v.getCityOrMunicipality(),
                    v.getBirthDate(),
                    v.hasVoted() ? "YES" : "NO",
                    v.getPassword()
            );
        }

        System.out.println("─".repeat(100));
        System.out.printf("Total: %d voters\n", voters.size());
    }

    private void displayAdminsRawData() {
        List<Admin> admins = userService.getAdmins();

        System.out.println("\nALL ADMINS (admins.dat)");
        System.out.println("═".repeat(100));
        System.out.printf("%-25s | %-15s | %-20s | %-12s | %-20s\n",
                "Name", "Province", "City", "Birth Date", "Password");
        System.out.println("─".repeat(100));

        for (Admin a : admins) {
            System.out.printf("%-25s | %-15s | %-20s | %-12s | %-20s\n",
                    a.getName(),
                    a.getProvince(),
                    a.getCityOrMunicipality(),
                    a.getBirthDate(),
                    a.getPassword()
            );
        }

        System.out.println("─".repeat(100));
        System.out.printf("Total: %d admins\n", admins.size());
    }

    private void displayCandidatesRawData() {
        List<Candidate> candidates = candidateService.getAllCandidates();

        System.out.println("\n ALL CANDIDATES (candidates.dat)");
        System.out.println("═".repeat(120));
        System.out.printf("%-25s | %-18s | %-15s | %-6s | %-10s\n",
                "Name", "Position", "Location", "Votes", "Status");
        System.out.println("─".repeat(120));

        for (Candidate c : candidates) {
            String status = "";
            if (c.isDisqualified()) status = "DISQUALIFIED";
            else if (c.hasWithdrawn()) status = "WITHDRAWN";
            else if (c.hasConceded()) status = "CONCEDED";
            else status = "ACTIVE";

            System.out.printf("%-25s | %-18s | %-15s | %-6d | %-10s\n",
                    c.getName(),
                    c.getPosition().name().replace("_", " "),
                    c.getLocation(),
                    c.getVotes(),
                    status
            );
        }

        System.out.println("─".repeat(120));
        System.out.printf("Total: %d candidates\n", candidates.size());
    }

    private void displayVotesRawData() {
        Collection<Vote> votes = voteService.getAllVotes();

        System.out.println("\n ALL VOTES (votes.dat)");
        System.out.println("═".repeat(100));

        if (votes.isEmpty()) {
            System.out.println("  No votes have been cast yet.");
            return;
        }

        int count = 1;
        for (Vote vote : votes) {
            System.out.println("\nVote #" + count);
            System.out.println("Voter ID: " + vote.getVoterId());
            System.out.println("Selections:");

            vote.getCandidateVotes().forEach((position, candidateName) -> {
                System.out.printf("  - %-20s: %s\n",
                        position.name().replace("_", " "),
                        candidateName);
            });

            System.out.println("─".repeat(100));
            count++;
        }

        System.out.printf("\nTotal Votes Cast: %d\n", votes.size());
    }

    private void displayElectionState() {
        boolean isOngoing = electionService.isElectionOngoing();

        System.out.println("\n⚙️  ELECTION STATE (election.dat)");
        System.out.println("═".repeat(60));

        String status = isOngoing ? "ONGOING" : "STOPPED";
        System.out.println("Current Status: " + status);

        System.out.println("═".repeat(60));
    }

    private void printCandidatesFor(Position pos) {
        List<Candidate> list = candidateService.getCandidatesByPosition(pos);
        System.out.println("\n Position: " + pos);
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


}
