package ph.robleding.votingsystem.service;

import ph.robleding.votingsystem.model.ElectionState;
import ph.robleding.votingsystem.util.FileConstants;
import ph.robleding.votingsystem.util.FileUtil;
import ph.robleding.votingsystem.model.Candidate;
import ph.robleding.votingsystem.util.ResultExporter;

import java.util.Scanner;

public class ElectionService {
    private final String FILE = FileConstants.ELECTION_FILE;
    private ElectionState state;
    // NEW - Service dependencies for resetting votes
    private CandidateService candidateService;
    private VoteService voteService;
    private UserService userService;

    public ElectionService() {
        loadState();
    }

    public void startElection() {
        // Clear previous election data
        if (candidateService != null && voteService != null && userService != null) {
            long voteCount = voteService.getAllVotes().size();

            if (voteCount > 0) {
                System.out.println(" Clearing " + voteCount + " votes from previous election...");

                // Reset all candidate vote counts
                for (Candidate c : candidateService.getAllCandidates()) {
                    c.resetVotes();
                }
                candidateService.saveAll();

                // Clear all vote records and reset voters
                voteService.clearAllVotes();
            }
        }

        state.setOngoing(true);
        saveState();
        System.out.println(" New election started with clean slate.");
    }

    public void stopElection() {
        state.setOngoing(false);
        saveState();
        System.out.println(" Election ended. Results are now available for viewing.");
    }
    // NEW - Method to inject services
    public void setServices(CandidateService candidateService, VoteService voteService, UserService userService) {
        this.candidateService = candidateService;
        this.voteService = voteService;
        this.userService = userService;
    }
    public boolean isElectionOngoing() {
        return state.isOngoing();
    }

    private void loadState() {
        state = FileUtil.loadSingle(FILE, ElectionState.class);
        if (state == null) state = new ElectionState();
    }

    private void saveState() {
        FileUtil.saveSingle(FILE, state);
    }
}
