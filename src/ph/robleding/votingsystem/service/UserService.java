package  ph.robleding.votingsystem.service;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import ph.robleding.votingsystem.model.Admin;
import ph.robleding.votingsystem.model.User;
import ph.robleding.votingsystem.model.Voter;
import ph.robleding.votingsystem.util.FileUtil;

public class UserService {
    private final String VOTERS_FILE = "voters.dat";
    private final String ADMINS_FILE = "admins.dat";

    private final List<Voter> voters;
    private final List<Admin> admins;

    public UserService() {
        this.voters = FileUtil.loadFromFile(VOTERS_FILE);
        this.admins = FileUtil.loadFromFile(ADMINS_FILE);
    }

    public void saveAll() {
        FileUtil.saveToFile(VOTERS_FILE, voters);
        FileUtil.saveToFile(ADMINS_FILE, admins);
    }

    public boolean isVoterUnique(String name, String birthDate, String province, String city) {
        return voters.stream().noneMatch(v ->
            v.getName().equalsIgnoreCase(name) &&
            v.getBirthDate().equals(birthDate) &&
            v.getProvince().equalsIgnoreCase(province) &&
            v.getCityOrMunicipality().equalsIgnoreCase(city));
    }


    public boolean registerVoter(String name, String province, String city, String birthDate, String password) {
        if (!isVoterUnique(name, birthDate, province, city)) return false;

        Voter voter = new Voter(name, province, city, birthDate, password);
        voters.add(voter);
        saveAll();
        appendVoterToCSV(voter);
        return true;
    }
    private void appendVoterToCSV(Voter voter) {
        String fileName = "voters.csv";
        boolean fileExists = new java.io.File(fileName).exists();

        try (FileWriter writer = new FileWriter(fileName, true)) {
            if (!fileExists) {
                writer.write("Name,Province,City,BirthDate,HasVoted\n");
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

        Optional<Voter> voter = voters.stream()
            .filter(v -> v.getName().equalsIgnoreCase(name) && v.authenticate(password))
            .findFirst();

        return voter.orElse(null);
    }


    public List<Voter> getVoters() {
        return voters;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
        saveAll();
    }
}
