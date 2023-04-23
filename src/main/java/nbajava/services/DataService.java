package nbajava.services;

import nbajava.models.Team;
import nbajava.models.TeamStats;
import org.htmlunit.html.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLTableElement;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    private final ApiService apiService;
    private final UtilityService utilityService;

    @Autowired
    public DataService(ApiService scraperService, UtilityService utilityService) {
        this.apiService = scraperService;
        this.utilityService = utilityService;
    }

    public Team getTeam(String city, String year) {
        final HtmlPage teamPage = this.apiService.getTeam(city, year);

        final HtmlTable table = (HtmlTable) teamPage.getElementById(city); // convenient to use the city param again
        final HtmlTableRow row = (HtmlTableRow) table.getBodies().get(0).getFirstChild();

        // elements containing the intended data
        final HtmlTableHeaderCell seasonCell = (HtmlTableHeaderCell) row.getElementsByAttribute("th", "data-stat", "season").get(0);
        final HtmlTableCell teamName = row.getFirstByXPath("//td[@data-stat='team_name']");
        final HtmlTableCell winLossPercentage = row.getFirstByXPath("//td[@data-stat='win_loss_pct']");
        final HtmlTableCell coach = row.getFirstByXPath("//td[@data-stat='coaches']");
        final HtmlTableCell topWinSharePlayer = row.getFirstByXPath("//td[@data-stat='top_ws']");

        // create the object
        Team team = new Team();
        team.setFullName(teamName.getTextContent().replace("*", "")); // * in the text indicates playoff season. remove it.
        team.setAbbreviation(city);
        team.setSeasonYear(seasonCell.getTextContent());
        team.setWinLossPercentage(winLossPercentage.getTextContent());
        team.setHeadCoach(coach.getTextContent());
        team.setTopWinSharePlayer(topWinSharePlayer.getTextContent());
        return team;
    }

    public ArrayList<String> getSeasonYears() {
        final HtmlPage teamPage = this.apiService.getSeasonYears();
        final HtmlTable table = (HtmlTable) teamPage.getElementById("stats");
        final HtmlTableBody tableBody = table.getBodies().get(0);
        final List<HtmlTableRow> rows = tableBody.getRows();

        ArrayList<String> seasons = new ArrayList<>();
        for (HtmlTableRow row : rows) {
            List<HtmlTableCell> rowCells = row.getElementsByAttribute("th", "data-stat", "season");
            if (!rowCells.isEmpty()) {
                final HtmlTableHeaderCell seasonCell = (HtmlTableHeaderCell) rowCells.get(0);
                seasons.add(seasonCell.getTextContent());
            }
        }

        return seasons;
    }

    public TeamStats getTeamStatsToCompareByTeam(String city, String year) {
        final Team team = this.getTeam(city, year);
        team.setYear(this.utilityService.getYearFromSeasonYear(team.getSeasonYear()));
        TeamStats ratingsPageStats = this.getTeamStatsFromRatingPage(team);
        return this.getTeamStatsFromTeamPage(ratingsPageStats);
    }

    public TeamStats getTeamStatsFromRatingPage(Team team) {
        final HtmlPage ratings = this.apiService.getTeamStatsFromRatingPage(team);

        final HtmlTable ratingsTable = (HtmlTable) ratings.getElementById("ratings");
        final HtmlTableBody ratingsTableBody = ratingsTable.getBodies().get(0);
        final List<HtmlTableRow> ratingsRows = ratingsTableBody.getRows();

        HtmlTableRow ratingsRow = null;
        for (HtmlTableRow row : ratingsRows) {
            if (row.getCell(1).getTextContent().equals(team.getFullName())) {
                ratingsRow = row;
                break;
            }
        }

        if (ratingsRow == null) {
            System.out.println("Could not find team in season ratings table.");
            return null;
        }

        final HtmlTableCell offensiveRating = ratingsRow.getFirstByXPath("//td[@data-stat='off_rtg']");
        final HtmlTableCell defensiveRating = ratingsRow.getFirstByXPath("//td[@data-stat='def_rtg']");

        TeamStats teamStats = new TeamStats();
        teamStats.setTeam(team);
        teamStats.setOffensiveRating(offensiveRating.getTextContent());
        teamStats.setDefensiveRating(defensiveRating.getTextContent());
        return teamStats;
    }

    public TeamStats getTeamStatsFromTeamPage(TeamStats teamStats) {
        Document document = this.apiService.getTeamStatsFromTeamPage(teamStats);
        Element teamAndOpponent = document.getElementById("team_and_opponent");

        Element teamTable = teamAndOpponent.select("tbody").get(0);
        Element teamRow = teamTable.select("tr").get(0);
        Element totalRebounds = teamRow.selectXpath("//td[@data-stat='trb']").first();

        Element opponentTable = teamAndOpponent.select("tbody").get(1);
        Element opponentRow = opponentTable.select("tr").get(0);
        Element opponentRebounds = opponentRow.selectXpath("//td[@data-stat='trb']").first();

        final String reboundDiff = String.valueOf(Double.parseDouble(totalRebounds.text()) -  Double.parseDouble(opponentRebounds.text()));
        teamStats.setReboundDifferential(reboundDiff);
        return teamStats;
    }
}
