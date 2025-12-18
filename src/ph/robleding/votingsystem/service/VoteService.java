package ph.robleding.votingsystem.service;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.Candidate;
import ph.robleding.votingsystem.model.Vote;
import ph.robleding.votingsystem.model.Voter;
import ph.robleding.votingsystem.util.FileUtil;

import java.util.*;

public class VoteService {
    private static final String VOTES_FILE = "votes.dat";

    private final Map<String, Vote> votes;

    public VoteService() {
        List<Vote> voteList = FileUtil.loadFromFile(VOTES_FILE);
        votes = new HashMap<>();
        for (Vote vote : voteList) {
            votes.put(vote.getVoterId(), vote);
        }
    }

    public void saveVotes() {
        FileUtil.saveToFile(VOTES_FILE, new ArrayList<>(votes.values()));
    }

    public boolean hasVoted(Voter voter) {
        return votes.containsKey(voter.getId());
    }

    public boolean castVote(Voter voter, Map<Position, String> selections, List<Candidate> candidates) {
        if (hasVoted(voter)) return false;

        Vote vote = new Vote(voter.getId());

        for (Map.Entry<Position, String> entry : selections.entrySet()) {
            Position pos = entry.getKey();
            String candName = entry.getValue();

            Optional<Candidate> candidate = candidates.stream()
                .filter(c -> c.getPosition() == pos && c.getName().equalsIgnoreCase(candName) && !c.isDisqualified() && !c.hasWithdrawn())
                .findFirst();

            if (candidate.isPresent()) {
                vote.addVote(pos, candidate.get().getName());
                candidate.get().addVote();
            } else {
                System.out.printf("⚠️ Invalid candidate '%s' for position %s. Ignoring.\n", candName, pos);
            }
        }

        if (!vote.isValid()) {
            System.out.println("⚠️ No valid votes found. Vote discarded.");
            return false;
        }

        votes.put(voter.getId(), vote);
        voter.setHasVoted(true);
        saveVotes();
        return true;
    }

    public Vote getReceipt(Voter voter) {
        return votes.get(voter.getId());
    }

    public Collection<Vote> getAllVotes() {
        return votes.values();
    }
}
