package nbajava.models;

public class Team {

    public String id;
    public String city;
    public String mascot;
    public String fullName;
    public String headCoach; // TODO replace with Coach object
    public String seasonYear; // TODO probably replace with a Season object
    public String offensiveRating;
    public String defensiveRating;
    public String winLossPercentage;
    public String topWinSharePlayer; // essentially the team's MVP. TODO replace with Player object

    public Team() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMascot() {
        return mascot;
    }

    public void setMascot(String mascot) {
        this.mascot = mascot;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHeadCoach() {
        return headCoach;
    }

    public void setHeadCoach(String headCoach) {
        this.headCoach = headCoach;
    }

    public String getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(String seasonYear) {
        this.seasonYear = seasonYear;
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

    public String getWinLossPercentage() {
        return winLossPercentage;
    }

    public void setWinLossPercentage(String winLossPercentage) {
        this.winLossPercentage = winLossPercentage;
    }

    public String getTopWinSharePlayer() {
        return topWinSharePlayer;
    }

    public void setTopWinSharePlayer(String topWinSharePlayer) {
        this.topWinSharePlayer = topWinSharePlayer;
    }
}
