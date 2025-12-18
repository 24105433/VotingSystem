package ph.robleding.votingsystem.ui;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.*;
import ph.robleding.votingsystem.service.*;
import ph.robleding.votingsystem.util.*;

import java.util.*;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final VoteService voteService = new VoteService();
    private final CandidateService candidateService = new CandidateService();
    private final ElectionService electionService = new ElectionService();

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

        switch (user.getRole()) {
            case "ADMIN" -> adminMenu((Admin) user);
            case "VOTER" -> voterMenu((Voter) user);
            case "CANDIDATE" -> candidateMenu((Candidate) user);
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

        boolean success = userService.registerVoter(name, birthDate, password, province, city);
        if (success) {
            System.out.println("✅ Registration successful!");
        } else {
            System.out.println("❌ A voter with the same details already exists.");
        }
    }

    private void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n🔐 Admin Menu");
            System.out.println("1. Start Election");
            System.out.println("2. Stop Election");
            System.out.println("3. Disqualify Candidate");
            System.out.println("4. View Tally");
            System.out.println("5. View Candidates");
            System.out.println("6. Manually Add Candidate");
            System.out.println("7. Purge All Voters & Candidates");//Remove all test users(voter/candidate) before starting the new election
            System.out.println("8. Logout");

            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> electionService.startElection();
                case "2" -> electionService.stopElection();
                case "3" -> disqualifyCandidate();
                case "4" -> viewTally();
                case "5" -> viewCandidates();
                case "6" -> addCandidateByAdmin();
                case "7" -> purgeAllData();
                case "8" -> {
                    System.out.println("👋 Logged out.");
                    return;
                }
                default -> System.out.println("❌ Invalid option.");
            }
        }
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
        System.out.println("📊 Tally Menu:");
        System.out.println("1. View all positions");
        System.out.println("2. View specific position");
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            for (Position pos : Position.values()) {
                printTallyFor(pos);
            }
        } else if ("2".equals(choice)) {
            System.out.print("Enter position: ");
            Position pos = Position.fromString(scanner.nextLine());
            if (pos != null) printTallyFor(pos);
            else System.out.println("❌ Invalid position.");
        } else {
            System.out.println("❌ Invalid option.");
        }
    }

    private void printTallyFor(Position pos) {
        List<Candidate> list = candidateService.getCandidatesByPosition(pos);
        System.out.println("\n📌 Position: " + pos);
        for (Candidate c : list) {
            System.out.printf("- %s: %d votes\n", c.getName(), c.getVotes());
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
        System.out.println("🔽 Enter your vote (leave blank to skip):");

        for (Position pos : Position.values()) {
            List<Candidate> candidates = candidateService.getCandidatesByPosition(pos)
                .stream()
                .filter(c -> {
                    if (pos == Position.PRESIDENT || pos == Position.VICE_PRESIDENT || pos == Position.SENATOR) {
                        return true;
                    } else if (pos == Position.GOVERNOR || pos == Position.VICE_GOVERNOR) {
                        return c.getProvince().equalsIgnoreCase(voter.getProvince());
                    } else {
                        return c.getProvince().equalsIgnoreCase(voter.getProvince())
                            && c.getCityOrMunicipality().equalsIgnoreCase(voter.getCityOrMunicipality());
                    }
                })
                .toList();

            System.out.println("\n🗳️ Candidates for " + pos.name() + ":");
            if (candidates.isEmpty()) {
                System.out.println("   ❌ No candidates running.");
                continue;
            }

            for (Candidate c : candidates) {
                System.out.println("   - " + c.getName() + " (" + c.getLocation() + ")");
            }

            System.out.print("Your vote for " + pos.name() + ": ");
            String name = scanner.nextLine().trim();

            if (!name.isBlank()) {
                selections.put(pos, name);
            }
        }


        boolean success = voteService.castVote(voter, selections, candidateService.getAllCandidates());

        if (success) {
            System.out.println("✅ Vote cast successfully.");
        } else {
            System.out.println("❌ Vote not cast. Maybe invalid or empty?");
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
