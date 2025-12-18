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
        for (Map.Entry<Position, String> entry : candidateVotes.entrySet()) {
            System.out.printf(" - %s: %s%n", entry.getKey(), entry.getValue());
        }
    }
}
