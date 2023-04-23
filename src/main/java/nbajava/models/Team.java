package nbajava.models;

public class Team {

    public String id;
    public String city;
    public String abbreviation;
    public String mascot;
    public String fullName;
    public String headCoach; // TODO replace with Coach object
    public String seasonYear; // YYYY-XX TODO probably replace with a Season object
    public String year; // YYYY
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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
