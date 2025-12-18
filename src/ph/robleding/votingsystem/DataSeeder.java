package ph.robleding.votingsystem;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.*;
import ph.robleding.votingsystem.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class DataSeeder {

    public static void main(String[] args) {
        seedAdmins();
        seedVotersAndCandidates();
        System.out.println("✅ Data seeded successfully!");
    }

    private static void seedAdmins() {
        List<Admin> admins = new ArrayList<>();
        admins.add(new Admin("admin1", "NCR", "Quezon City", "1970-01-01", "adminpass"));
        admins.add(new Admin("superadmin", "NCR", "Manila", "1969-12-31", "root"));

        FileUtil.saveToFile("admins.dat", admins);
    }

    private static void seedVotersAndCandidates() {
        List<Voter> voters = new ArrayList<>();
        List<Candidate> candidates = new ArrayList<>();

        // 🗳️ Sample Voters
        Voter voter1 = new Voter("Juan Dela Cruz", "NCR", "Quezon City", "2000-05-15", "juan123");
        Voter voter2 = new Voter("Maria Clara", "NCR", "Manila", "1999-09-09", "maria321");
        Voter voter3 = new Voter("Jose Rizal", "CALABARZON", "Calamba", "1998-06-19", "josehero");
        Voter voter4 = new Voter("Andres Bonifacio", "NCR", "Tondo", "1997-11-30", "revolta");

        voters.add(voter1);
        voters.add(voter2);
        voters.add(voter3);
        voters.add(voter4);

        // 🧑‍💼 Sample Candidates
        Candidate cand1 = new Candidate("Luna Antonio", "Ilocos Region", "San Fernando", "1985-01-02", "antipass", Position.PRESIDENT, "Nationwide");
        Candidate cand2 = new Candidate("Gregoria de Jesus", "NCR", "Malabon", "1988-02-20", "gregoria88", Position.VICE_PRESIDENT, "Nationwide");
        Candidate cand3 = new Candidate("Emilio Aguinaldo", "CALABARZON", "Kawit", "1980-03-22", "aguinaldo", Position.GOVERNOR, "Cavite");

        // 🧑‍💼 Voter-candidate mix (Maria Clara is also a senator)
        Candidate cand4 = new Candidate("Maria Clara", "NCR", "Manila", "1999-09-09", "maria321", Position.SENATOR, "NCR");

        candidates.add(cand1);
        candidates.add(cand2);
        candidates.add(cand3);
        candidates.add(cand4);

        // 💾 Save to files
        FileUtil.saveToFile("voters.dat", voters);
        FileUtil.saveToFile("candidates.dat", candidates);

        // Optional: Export CSVs if needed
        // VoterExporter.exportAll(voters);
        // CandidateExporter.exportAll(candidates);
    }
}
