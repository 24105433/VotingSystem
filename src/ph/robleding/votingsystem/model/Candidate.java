package ph.robleding.votingsystem.model;

import ph.robleding.votingsystem.enums.Position;

public class Candidate extends Voter {

    private final Position position;
    private final String location;

    private int votes;
    private boolean disqualified;
    private boolean conceded;
    private boolean withdrawn;

    // ✅ MAIN constructor (used everywhere: UI, CSV, seeder, service)
    public Candidate(
            String name,
            String province,
            String cityOrMunicipality,
            String birthDate,
            String password,
            Position position,
            String location
    ) {
        super(name, province, cityOrMunicipality, birthDate, password);
        this.position = position;
        this.location = location;
        this.votes = 0;
        this.disqualified = false;
        this.conceded = false;
        this.withdrawn = false;
    }
    public void resetVotes() {
        this.votes = 0;
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
