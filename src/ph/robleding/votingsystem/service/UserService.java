package ph.robleding.votingsystem.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.io.File;

import ph.robleding.votingsystem.model.Admin;
import ph.robleding.votingsystem.model.User;
import ph.robleding.votingsystem.model.Voter;
import ph.robleding.votingsystem.util.FileConstants;
import ph.robleding.votingsystem.util.FileUtil;

public class UserService {
    // ✅ Use FileConstants instead of hardcoded strings
    private final String VOTERS_FILE = FileConstants.VOTERS_FILE;
    private final String ADMINS_FILE = FileConstants.ADMINS_FILE;

    private final List<Voter> voters;
    private final List<Admin> admins;

    // ✅ Constructor to load data
    public UserService() {
        this.voters = FileUtil.loadFromFile(VOTERS_FILE);
        this.admins = FileUtil.loadFromFile(ADMINS_FILE);
    }

    public void saveAll() {
        FileUtil.saveToFile(VOTERS_FILE, voters);
        FileUtil.saveToFile(ADMINS_FILE, admins);
    }

    public boolean isVoterUnique(String name, String birthDate) {
        return voters.stream().noneMatch(v ->
                v.getName().equalsIgnoreCase(name) &&
                        v.getBirthDate().equals(birthDate));
    }

    public boolean registerVoter(String name, String province, String city, String birthDate, String password) {
        if (!isVoterUnique(name, birthDate)) {
            System.out.println("⚠️ A voter with this name and birthdate already exists.");
            return false;
        }

        Voter voter = new Voter(name, province, city, birthDate, password);
        voters.add(voter);
        saveAll();
        appendVoterToCSV(voter);
        return true;
    }

    private void appendVoterToCSV(Voter voter) {
        String fileName = FileConstants.VOTERS_CSV;
        boolean fileExists = new File(fileName).exists();

        try (FileWriter writer = new FileWriter(fileName, true)) {
            if (!fileExists) {
                writer.write("Name,Province,City/Municipality,BirthDate,HasVoted\n");
            }

            writer.write(String.format("%s,%s,%s,%s,%s\n",
                    voter.getName(),
                    voter.getProvince(),
                    voter.getCityOrMunicipality(),
                    voter.getBirthDate(),
                    voter.hasVoted() ? "YES" : "NO"
            ));
        } catch (IOException e) {
            System.out.println("❌ Failed to append voter to CSV: " + e.getMessage());
        }
    }

    public User login(String name, String password) {
        System.out.println("Trying to login: " + name);

        for (Admin admin : admins) {
            System.out.println("Checking admin: " + admin.getName());
        }

        Optional<Admin> admin = admins.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name) && a.authenticate(password))
                .findFirst();

        if (admin.isPresent()) {
            return admin.get();
        }

        // ✅ Only return voters (candidates log in as voters)
        Optional<Voter> voter = voters.stream()
                .filter(v -> v.getName().equalsIgnoreCase(name) && v.authenticate(password))
                .findFirst();

        return voter.orElse(null);
    }

    public List<Voter> getVoters() {
        return voters;
    }

    public void addVoter(Voter voter) {
        voters.add(voter);
        saveAll();
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
        saveAll();
    }
}