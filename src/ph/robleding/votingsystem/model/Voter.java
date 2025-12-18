package ph.robleding.votingsystem.model;

public class Voter extends User {
    private boolean hasVoted;

    public Voter(String name, String province, String city, String birthDate, String password) {
        super(name, province, city, birthDate, password);
        this.hasVoted = false;
    }

    @Override
    public String getRole() {
        return "VOTER";
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
