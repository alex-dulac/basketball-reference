package nbajava.services;

import nbajava.models.Team;
import nbajava.models.TeamStats;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {

    private static final String baseUrl = "https://www.basketball-reference.com/";
    private static final String teamsPage = "teams/";
    private static final String leaguesPage = "leagues/";
    private static final String ratingsPrefix = "NBA_";
    private static final String ratingsSuffix = "_ratings.html";

    public Team getTeam(String city, String year) {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        final String url = baseUrl + teamsPage + city;

        try {
            final HtmlPage teamPage = client.getPage(url);
            final HtmlTable table = (HtmlTable) teamPage.getElementById(city); // convenient to use the city param again
            final HtmlTableRow row = (HtmlTableRow) table.getBodies().get(0).getFirstChild();

            // elements containing the intended data
            final HtmlTableHeaderCell seasonCell = (HtmlTableHeaderCell) row.getElementsByAttribute("th", "data-stat", "season").get(0);
            final HtmlTableCell teamName = row.getFirstByXPath("//td[@data-stat='team_name']");
            final HtmlTableCell winLossPercentage = row.getFirstByXPath("//td[@data-stat='win_loss_pct']");
            final HtmlTableCell coach = row.getFirstByXPath("//td[@data-stat='coaches']");
            final HtmlTableCell topWinSharePlayer = row.getFirstByXPath("//td[@data-stat='top_ws']");

            // create the object
            Team teamObject = new Team();
            teamObject.setFullName(teamName.getTextContent().replace("*", "")); // * in the text indicates playoff season. remove it.
            teamObject.setAbbreviation(city);
            teamObject.setSeasonYear(seasonCell.getTextContent());
            teamObject.setWinLossPercentage(winLossPercentage.getTextContent());
            teamObject.setHeadCoach(coach.getTextContent());
            teamObject.setTopWinSharePlayer(topWinSharePlayer.getTextContent());

            return teamObject;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<String> getSeasonYears() {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        final String url = baseUrl + leaguesPage;

        try {
            final HtmlPage teamPage = client.getPage(url);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public TeamStats getTeamStatsFromRatingPage(Team team) {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        final String url = baseUrl + leaguesPage + ratingsPrefix + team.getYear() + ratingsSuffix;

        try {
            // stats for the first page
            final HtmlPage ratings = client.getPage(url);
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

            if (ratingsRow.equals(null)) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public TeamStats getTeamStatsFromTeamPage(TeamStats teamStats) {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        final String url = baseUrl + teamsPage + teamStats.team.abbreviation + "/" + teamStats.team.getYear() + ".html";

        try {
            // stats for the second page
            final HtmlPage teamPage = client.getPage(url);
            final HtmlTable teamAndOpponent = (HtmlTable) teamPage.getElementById("team_and_opponent");
            final HtmlTableBody teamStatsBody = teamAndOpponent.getBodies().get(0);
            final HtmlTableBody opponentsStatsBody = teamAndOpponent.getBodies().get(1);

            final HtmlTableRow teamStatsRow = teamStatsBody.getFirstByXPath("//tr[text()='Team']");
            final HtmlTableCell totalRebounds = teamStatsRow.getFirstByXPath("//td[@data-stat='trb']");

            final HtmlTableRow opponentStatsRow = opponentsStatsBody.getFirstByXPath("//tr[text()='Opponent']");
            final HtmlTableCell opponentsRebounds = opponentStatsRow.getFirstByXPath("//td[@data-stat='trb']");

            // Strings to Doubles to one String ... yeah
            final String reboundDiff = String.valueOf(Double.parseDouble(totalRebounds.getTextContent()) -  Double.parseDouble(opponentsRebounds.getTextContent()));

            teamStats.setReboundDifferential(reboundDiff);

            return teamStats;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
