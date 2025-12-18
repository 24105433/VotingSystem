package ph.robleding.votingsystem.util;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.Candidate;
import ph.robleding.votingsystem.service.CandidateService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ResultExporter {

    public static void exportResults(CandidateService candidateService) {
        String file = "results.csv";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Position,Candidate,Location,Votes,Conceded,Withdrawn,Disqualified\n");

            for (Position position : Position.values()) {
                List<Candidate> candidates = candidateService.getAllCandidates();

                for (Candidate c : candidates) {
                    if (c.getPosition() == position) {
                        writer.write(String.format("%s,%s,%s,%d,%s,%s,%s\n",
                                position,
                                c.getName(),
                                c.getLocation(),
                                c.getVotes(),
                                c.hasConceded(),
                                c.hasWithdrawn(),
                                c.isDisqualified()
                        ));
                    }
                }
            }

            System.out.println("✅ Election results exported to results.csv");
        } catch (IOException e) {
            System.out.println("❌ Failed to export results: " + e.getMessage());
        }
    }
}
