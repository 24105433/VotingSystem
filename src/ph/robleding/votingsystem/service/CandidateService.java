package ph.robleding.votingsystem.service;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.Candidate;
import ph.robleding.votingsystem.model.Vote;
import ph.robleding.votingsystem.model.Voter;
import ph.robleding.votingsystem.util.*;

import java.util.*;

public class CandidateService {
    private static final String CANDIDATES_FILE = FileConstants.CANDIDATES_FILE;
    private final List<Candidate> candidates;
    private UserService userService; //  Add this field

    public CandidateService() {
        this.candidates = FileUtil.loadFromFile(CANDIDATES_FILE);
    }

    //  Add this method to inject UserService
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void saveAll() {
        FileUtil.saveToFile(CANDIDATES_FILE, candidates);
    }
    // Reload candidates from file (use before viewing tally)
    public void reload() {
        candidates.clear();
        List<Candidate> freshData = FileUtil.loadFromFile(CANDIDATES_FILE);
        candidates.addAll(freshData);
    }
    public List<Candidate> getAllCandidates() {
        return candidates;
    }

    public List<Candidate> getCandidatesByPosition(Position position) {
        return candidates.stream()
                .filter(c -> c.getPosition() == position)
                .toList();
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
        saveAll();

        //  Also add/update as voter
        if (userService != null) {
            ensureCandidateIsVoter(candidate);
        }
    }

    public boolean submitCandidacy(Voter voter, Position position, String location) {
        Optional<Candidate> existing = candidates.stream()
                .filter(c -> c.getName().equalsIgnoreCase(voter.getName()))
                .filter(c -> c.getPosition() == position)
                .findFirst();

        if (existing.isPresent()) return false;

        Candidate candidate = new Candidate(
                voter.getName(),
                voter.getProvince(),
                voter.getCityOrMunicipality(),
                voter.getBirthDate(),
                voter.getPassword(),
                position,
                location
        );

        candidates.add(candidate);
        saveAll();

        // Voter already exists since they're submitting candidacy
        return true;
    }

    //  Add this helper method
    private void ensureCandidateIsVoter(Candidate candidate) {
        List<Voter> voters = userService.getVoters();

        // Check if voter already exists
        boolean voterExists = voters.stream()
                .anyMatch(v -> v.getName().equalsIgnoreCase(candidate.getName()) &&
                        v.getBirthDate().equals(candidate.getBirthDate()));

        if (!voterExists) {
            // Add candidate as voter
            Voter newVoter = new Voter(
                    candidate.getName(),
                    candidate.getProvince(),
                    candidate.getCityOrMunicipality(),
                    candidate.getBirthDate(),
                    candidate.getPassword()
            );
            userService.addVoter(newVoter);
        }
    }

    public void disqualify(String name, Position position) {
        candidates.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .filter(c -> c.getPosition() == position)
                .findFirst()
                .ifPresent(Candidate::disqualify);
        saveAll();
    }

    public void withdraw(String name, Position position) {
        candidates.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .filter(c -> c.getPosition() == position)
                .findFirst()
                .ifPresent(Candidate::withdraw);
        saveAll();
    }

    public void concede(String name, Position position) {
        candidates.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .filter(c -> c.getPosition() == position)
                .findFirst()
                .ifPresent(Candidate::concede);
        saveAll();
    }

    public void rebuildVoteCountsFromVotes(Collection<Vote> allVotes) {
        // Reset all vote counts to 0
        for (Candidate c : candidates) {
            c.resetVotes();
        }

        // Recount votes from vote records
        for (Vote vote : allVotes) {
            Map<Position, String> candidateVotes = vote.getCandidateVotes();
            for (Map.Entry<Position, String> entry : candidateVotes.entrySet()) {
                Position pos = entry.getKey();
                String candidateNames = entry.getValue();

                // Handle multiple selections (comma-separated)
                String[] names = candidateNames.split(",");
                for (String name : names) {
                    String trimmedName = name.trim();

                    candidates.stream()
                            .filter(c -> c.getPosition() == pos)
                            .filter(c -> c.getName().equalsIgnoreCase(trimmedName))
                            .findFirst()
                            .ifPresent(Candidate::addVote);
                }
            }
        }

        saveAll();
    }
}