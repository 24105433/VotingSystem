package ph.robleding.votingsystem.service;

import ph.robleding.votingsystem.model.ElectionState;
import ph.robleding.votingsystem.util.FileUtil;

public class ElectionService {
    private final String FILE = "election.dat";
    private ElectionState state;

    public ElectionService() {
        loadState();
    }

    public void startElection() {
        state.setOngoing(true);
        saveState();
        System.out.println("✅ Election started.");
    }

    public void stopElection() {
        state.setOngoing(false);
        saveState();
        System.out.println("🛑 Election ended.");
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
