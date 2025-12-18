package ph.robleding.votingsystem.model;

import java.io.Serializable;

public class ElectionState implements Serializable {
    private boolean isOngoing;

    public ElectionState() {
        this.isOngoing = false;
    }

    public boolean isOngoing() {
        return isOngoing;
    }

    public void setOngoing(boolean ongoing) {
        isOngoing = ongoing;
    }
}
