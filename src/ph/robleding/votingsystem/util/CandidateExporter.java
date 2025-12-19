package ph.robleding.votingsystem.util;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.Candidate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateExporter {  // ✅ Make sure this line exists and is correct

    public static void appendCandidate(Candidate candidate) {
        // ... your code ...
    }

    public static void exportAll(List<Candidate> candidates) {
        // ... your code ...
    }

    public static List<Candidate> importFromCSV(String fileName) {
        List<Candidate> candidates = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("⚠️ CSV file not found: " + fileName);
            return candidates;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;

            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 9) continue;

                String name = parts[0];
                String province = parts[1];
                String city = parts[2];
                String birthDate = parts[3];
                Position position = Position.valueOf(parts[4]);
                String location = parts[5];

                boolean disqualified = parts[6].equalsIgnoreCase("YES");
                boolean withdrawn = parts[7].equalsIgnoreCase("YES");
                boolean conceded = parts[8].equalsIgnoreCase("YES");

                // ✅ FIXED: Correct parameter order
                Candidate candidate = new Candidate(name, province, city, birthDate, "default", position, location);

                if (disqualified) candidate.disqualify();
                if (withdrawn) candidate.withdraw();
                if (conceded) candidate.concede();

                candidates.add(candidate);
            }

            System.out.println("✅ Imported " + candidates.size() + " candidates from CSV");

        } catch (Exception e) {
            System.out.println("❌ Failed to import candidates: " + e.getMessage());
        }

        return candidates;
    }
}