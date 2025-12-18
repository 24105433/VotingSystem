package ph.robleding.votingsystem.model;

import ph.robleding.votingsystem.enums.Position;

public class Candidate extends Voter {
    private final Position position;
    private final String location;
    private int votes;
    private boolean disqualified;
    private boolean conceded;
    private boolean withdrawn;

    public Candidate(String name, String province, String city, String birthDate, String password, Position position, String location) {
        super(name, province, city, birthDate, password);
        this.position = position;
        this.location = location;
        this.votes = 0;
        this.disqualified = false;
        this.conceded = false;
        this.withdrawn = false;
    }

    @Override
    public String getRole() {
        return "CANDIDATE";
    }

    public Position getPosition() {
        return position;
    }

    public String getLocation() {
        return location;
    }

    public int getVotes() {
        return votes;
    }

    public void addVote() {
        this.votes++;
    }

    public boolean isDisqualified() {
        return disqualified;
    }

    public void disqualify() {
        this.disqualified = true;
    }

    public boolean hasConceded() {
        return conceded;
    }

    public void concede() {
        this.conceded = true;
    }

    public boolean hasWithdrawn() {
        return withdrawn;
    }

    public void withdraw() {
        this.withdrawn = true;
    }
}
