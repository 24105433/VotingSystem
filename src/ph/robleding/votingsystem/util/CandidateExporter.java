package ph.robleding.votingsystem.util;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.Candidate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateExporter {

    private static final String FILE_NAME = "candidates.csv";

    // ✅ Append a single candidate
    public static void appendCandidate(Candidate candidate) {
        boolean fileExists = new File(FILE_NAME).exists();

        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            if (!fileExists) {
                writer.write("Name,Province,City,BirthDate,Position,Location,Disqualified,Withdrawn,Conceded\n");
            }

            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    candidate.getName(),
                    candidate.getProvince(),
                    candidate.getCityOrMunicipality(),
                    candidate.getBirthDate(),
                    candidate.getPosition().name(),
                    candidate.getLocation(),
                    candidate.isDisqualified() ? "YES" : "NO",
                    candidate.hasWithdrawn() ? "YES" : "NO",
                    candidate.hasConceded() ? "YES" : "NO"
            ));
        } catch (IOException e) {
            System.out.println("❌ Failed to write candidate to CSV: " + e.getMessage());
        }
    }

    // ✅ Overwrite all candidates
    public static void exportAll(List<Candidate> candidates) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write("Name,Province,City,BirthDate,Position,Location,Disqualified,Withdrawn,Conceded\n");

            for (Candidate c : candidates) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        c.getName(),
                        c.getProvince(),
                        c.getCityOrMunicipality(),
                        c.getBirthDate(),
                        c.getPosition().name(),
                        c.getLocation(),
                        c.isDisqualified() ? "YES" : "NO",
                        c.hasWithdrawn() ? "YES" : "NO",
                        c.hasConceded() ? "YES" : "NO"
                ));
            }

            System.out.println("✅ Exported all candidates to: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("❌ Failed to export candidates: " + e.getMessage());
        }
    }

    // ✅ Import from CSV
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
                    continue; // Skip header
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

                // Use default password since it's not stored
                Candidate candidate = new Candidate(
                        name, province, city, birthDate, "default", position, location
                );

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
