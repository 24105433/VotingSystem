package ph.robleding.votingsystem.util;

import ph.robleding.votingsystem.model.Voter;

import java.io.*;
import java.util.List;

public class VoterExporter {

    public static void appendVoter(Voter voter) {
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

    public static void exportAll(List<Voter> voters) {
        String fileName = FileConstants.VOTERS_CSV;
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Name,Province,City/Municipality,BirthDate,HasVoted\n");
            for (Voter v : voters) {
                writer.write(String.format("%s,%s,%s,%s,%s\n",
                        v.getName(),
                        v.getProvince(),
                        v.getCityOrMunicipality(),
                        v.getBirthDate(),
                        v.hasVoted() ? "YES" : "NO"
                ));
            }
            System.out.println("✅ Exported " + voters.size() + " voters to: " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Failed to export voters: " + e.getMessage());
        }
    }
}