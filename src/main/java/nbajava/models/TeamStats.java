package nbajava.models;

public class TeamStats {

    public String id;
    public Team team;
    public String offensiveRating;
    public String defensiveRating;
    public String reboundDifferential;

    public TeamStats() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getOffensiveRating() {
        return offensiveRating;
    }

    public void setOffensiveRating(String offensiveRating) {
        this.offensiveRating = offensiveRating;
    }

    public String getDefensiveRating() {
        return defensiveRating;
    }

    public void setDefensiveRating(String defensiveRating) {
        this.defensiveRating = defensiveRating;
    }

    public String getReboundDifferential() {
        return reboundDifferential;
    }

    public void setReboundDifferential(String reboundDifferential) {
        this.reboundDifferential = reboundDifferential;
    }
}
