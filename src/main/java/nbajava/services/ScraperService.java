package nbajava.services;

import nbajava.models.Team;
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

    public Team getTeam(String city, String year) {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        final String url = baseUrl + teamsPage + city;

        try {
            final HtmlPage teamPage = client.getPage(url);
            final HtmlTable table = (HtmlTable) teamPage.getElementById(city); // convenient to use the city param again
            final HtmlTableRow dataRow = (HtmlTableRow) table.getBodies().get(0).getFirstChild();

            // elements containing the intended data
            final HtmlTableHeaderCell seasonCell = (HtmlTableHeaderCell) dataRow.getElementsByAttribute("th", "data-stat", "season").get(0);
            final HtmlTableCell teamName = dataRow.getFirstByXPath("//td[@data-stat='team_name']");
            final HtmlTableCell winLossPercentage = dataRow.getFirstByXPath("//td[@data-stat='win_loss_pct']");
            final HtmlTableCell offensiveRating = dataRow.getFirstByXPath("//td[@data-stat='off_rtg']");
            final HtmlTableCell defensiveRating = dataRow.getFirstByXPath("//td[@data-stat='def_rtg']");
            final HtmlTableCell coach = dataRow.getFirstByXPath("//td[@data-stat='coaches']");
            final HtmlTableCell topWinSharePlayer = dataRow.getFirstByXPath("//td[@data-stat='top_ws']");

            // create the object
            Team teamObject = new Team();
            teamObject.setFullName(teamName.getTextContent());
            teamObject.setSeasonYear(seasonCell.getTextContent());
            teamObject.setOffensiveRating(offensiveRating.getTextContent());
            teamObject.setDefensiveRating(defensiveRating.getTextContent());
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
}
