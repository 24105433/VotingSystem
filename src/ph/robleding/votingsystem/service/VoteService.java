package ph.robleding.votingsystem.service;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.Candidate;
import ph.robleding.votingsystem.model.Vote;
import ph.robleding.votingsystem.model.Voter;
import ph.robleding.votingsystem.util.FileUtil;

import java.util.*;

public class VoteService {
    private static final String VOTES_FILE = "votes.dat";
    private final CandidateService candidateService;
    private final UserService userService;
    private final Map<String, Vote> votes;

    public VoteService(CandidateService candidateService, UserService userService) {
        this.candidateService = candidateService;
        this.userService = userService;

        // Load existing votes from file
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

    // Update the castVote method in VoteService.java:

    public boolean castVote(Voter voter, Map<Position, String> selections, List<Candidate> allCandidates) {
        if (selections.isEmpty()) return false;

        Vote vote = new Vote(voter.getId());
        boolean hasValidVote = false;

        for (Map.Entry<Position, String> entry : selections.entrySet()) {
            Position pos = entry.getKey();
            String candidateNames = entry.getValue();

            // Handle multiple selections (comma-separated) for Senator and Councilor
            String[] names = candidateNames.split(",");

            for (String name : names) {
                String trimmedName = name.trim();

                Optional<Candidate> candidate = allCandidates.stream()
                        .filter(c -> c.getPosition() == pos)
                        .filter(c -> c.getName().equalsIgnoreCase(trimmedName))
                        .filter(c -> !c.isDisqualified() && !c.hasWithdrawn())
                        .findFirst();

                if (candidate.isPresent()) {
                    Candidate c = candidate.get();
                    c.addVote();  // Increment vote count
                    hasValidVote = true;
                }
            }

            // Add the full selection string to vote record (for receipt)
            if (hasValidVote) {
                vote.addVote(pos, candidateNames);
            }
        }

        if (hasValidVote) {
            votes.put(voter.getId(), vote);
            voter.setHasVoted(true);

            // Save all data
            saveVotes();
            candidateService.saveAll();
            userService.saveAll();

            return true;
        }

        return false;
    }

    public Vote getReceipt(Voter voter) {
        return votes.get(voter.getId());
    }

    public Collection<Vote> getAllVotes() {
        return votes.values();
    }
}