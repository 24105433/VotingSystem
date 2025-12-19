package ph.robleding.votingsystem.util;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.Candidate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateExporter {  // ✅ Make sure this line exists and is correct

    public static void appendCandidate(Candidate candidate) {
        String fileName = FileConstants.CANDIDATES_CSV;
        boolean fileExists = new File(fileName).exists();

        try (FileWriter writer = new FileWriter(fileName, true)) {
            if (!fileExists) {
                writer.write("Name,Province,City/Municipality,BirthDate,Position,Location,Disqualified,Withdrawn,Conceded\n");
            }

            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    candidate.getName(),
                    candidate.getProvince(),
                    candidate.getCityOrMunicipality(),
                    candidate.getBirthDate(),
                    candidate.getPosition(),
                    candidate.getLocation(),
                    candidate.isDisqualified() ? "YES" : "NO",
                    candidate.hasWithdrawn() ? "YES" : "NO",
                    candidate.hasConceded() ? "YES" : "NO"
            ));
        } catch (IOException e) {
            System.out.println("❌ Failed to append candidate to CSV: " + e.getMessage());
        }
    }

    public static void exportAll(List<Candidate> candidates) {
        String fileName = FileConstants.CANDIDATES_CSV;
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Name,Province,City/Municipality,BirthDate,Position,Location,Disqualified,Withdrawn,Conceded\n");

            for (Candidate c : candidates) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        c.getName(),
                        c.getProvince(),
                        c.getCityOrMunicipality(),
                        c.getBirthDate(),
                        c.getPosition(),
                        c.getLocation(),
                        c.isDisqualified() ? "YES" : "NO",
                        c.hasWithdrawn() ? "YES" : "NO",
                        c.hasConceded() ? "YES" : "NO"
                ));
            }
            System.out.println("✅ Exported " + candidates.size() + " candidates to: " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Failed to export candidates: " + e.getMessage());
        }
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