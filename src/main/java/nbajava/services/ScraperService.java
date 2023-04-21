package nbajava.services;

import nbajava.models.Team;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.springframework.stereotype.Service;

@Service
public class ScraperService {

    private static final String baseUrl = "https://www.basketball-reference.com/";
    private static final String teamsPage = "teams/";

    public Team getTeam(String city, String year) {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        final String url = baseUrl + teamsPage + city;

        try {
            // would be great if all elements were ID'ed , but it's not that simple (probably intentionally)
            final HtmlPage teamPage = client.getPage(url);
            final HtmlTable table = (HtmlTable) teamPage.getElementById(city); // convenient to use the city again


            HtmlTableRow dataRow;
            if (year.isEmpty()) {
                dataRow = (HtmlTableRow) table.getBodies().get(0).getFirstChild(); // first row is the latest season by default
            } else {
                // TODO update
                dataRow = (HtmlTableRow) table.getBodies().get(0).getFirstChild(); // first row is the latest season by default
            }

            // elements containing the intended data
            final HtmlTableHeaderCell seasonCell = dataRow.getFirstByXPath("//th[@data-stat='season']");
            // final String seasonYear = seasonCell.getElementsByTagName("<a>").get(0).getTextContent();
            final HtmlTableCell teamName = dataRow.getFirstByXPath("//td[@data-stat='team_name']");
            final HtmlTableCell winLossPercentage = dataRow.getFirstByXPath("//td[@data-stat='win_loss_pct']");
            final HtmlTableCell offensiveRating = dataRow.getFirstByXPath("//td[@data-stat='off_rtg']");
            final HtmlTableCell defensiveRating = dataRow.getFirstByXPath("//td[@data-stat='def_rtg']");

            // create the object
            Team teamObject = new Team();
            teamObject.setFullName(teamName.getTextContent());
            teamObject.setSeasonYear(seasonCell.getTextContent());
            teamObject.setOffensiveRating(offensiveRating.getVisibleText());
            teamObject.setDefensiveRating(defensiveRating.getVisibleText());
            teamObject.setWinLossPercentage(winLossPercentage.getTextContent());
            return teamObject;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
