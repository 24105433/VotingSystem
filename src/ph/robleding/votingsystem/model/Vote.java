package ph.robleding.votingsystem.model;

import ph.robleding.votingsystem.enums.Position;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Vote implements Serializable {
    private final String voterId;
    private final Map<Position, String> candidateVotes; // Position -> Candidate Name

    public Vote(String voterId) {
        this.voterId = voterId;
        this.candidateVotes = new HashMap<>();
    }

    public void addVote(Position position, String candidateName) {
        candidateVotes.put(position, candidateName);
    }

    public Map<Position, String> getCandidateVotes() {
        return candidateVotes;
    }

    public String getVoterId() {
        return voterId;
    }

    public boolean isValid() {
        return !candidateVotes.isEmpty();
    }

    public void printReceipt() {
        System.out.println("🧾 Vote Receipt:");

        // Print in strict hierarchy order
        if (candidateVotes.containsKey(Position.PRESIDENT)) {
            System.out.printf(" - %s: %s%n", Position.PRESIDENT, candidateVotes.get(Position.PRESIDENT));
        }
        if (candidateVotes.containsKey(Position.VICE_PRESIDENT)) {
            System.out.printf(" - %s: %s%n", Position.VICE_PRESIDENT, candidateVotes.get(Position.VICE_PRESIDENT));
        }
        if (candidateVotes.containsKey(Position.SENATOR)) {
            System.out.printf(" - %s: %s%n", Position.SENATOR, candidateVotes.get(Position.SENATOR));
        }
        if (candidateVotes.containsKey(Position.GOVERNOR)) {
            System.out.printf(" - %s: %s%n", Position.GOVERNOR, candidateVotes.get(Position.GOVERNOR));
        }
        if (candidateVotes.containsKey(Position.VICE_GOVERNOR)) {
            System.out.printf(" - %s: %s%n", Position.VICE_GOVERNOR, candidateVotes.get(Position.VICE_GOVERNOR));
        }
        if (candidateVotes.containsKey(Position.MAYOR)) {
            System.out.printf(" - %s: %s%n", Position.MAYOR, candidateVotes.get(Position.MAYOR));
        }
        if (candidateVotes.containsKey(Position.VICE_MAYOR)) {
            System.out.printf(" - %s: %s%n", Position.VICE_MAYOR, candidateVotes.get(Position.VICE_MAYOR));
        }
        if (candidateVotes.containsKey(Position.COUNCILOR)) {
            System.out.printf(" - %s: %s%n", Position.COUNCILOR, candidateVotes.get(Position.COUNCILOR));
        }
    }
}
